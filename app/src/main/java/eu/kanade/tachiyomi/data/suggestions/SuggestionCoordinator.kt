package eu.kanade.tachiyomi.data.suggestions

import eu.kanade.domain.source.service.SourcePreferences
import eu.kanade.tachiyomi.data.suggestions.sources.AniListRecommendationSource
import eu.kanade.tachiyomi.data.suggestions.sources.MangaUpdatesSimilarSource
import eu.kanade.tachiyomi.data.suggestions.sources.MyAnimeListRecommendationSource
import eu.kanade.tachiyomi.data.suggestions.sources.NovelUpdatesSimilarSource
import eu.kanade.tachiyomi.data.suggestions.sources.RecommendationPagingSource
import eu.kanade.tachiyomi.data.suggestions.sources.SuggestionMediaType
import eu.kanade.tachiyomi.data.suggestions.util.bestMatchScoreFor
import eu.kanade.tachiyomi.data.suggestions.util.dedupeByCleanTitle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import tachiyomi.core.common.preference.InMemoryPreferenceStore
import tachiyomi.core.common.util.system.logcat
import uy.kohesive.injekt.api.get

class SuggestionCoordinator(
    // Defaulted to a permissive in-memory shim so unit tests can construct
    // a coordinator without an Injekt registry. In production this is
    // overwritten by the [Injekt.get()] lookup.
    private val sourcePreferences: SourcePreferences = SourcePreferences(InMemoryPreferenceStore()),
) {

    /**
     * Build the set of external recommendation sources for [mediaType].
     *
     * - AniList is always included (covers MANGA, ANIME, and NOVEL via
     *   AniList's "Novel" format on the MANGA type).
     * - MAL is only added for ANIME.
     * - MangaUpdates is added for both MANGA and NOVEL (it stores light
     *   novels under type "Novel") — gated by the
     *   [SourcePreferences.suggestionsUseMangaUpdatesNovel] flag.
     * - NovelUpdates is added for NOVEL only — gated by
     *   [SourcePreferences.suggestionsUseNovelUpdates].
     */
    fun createSources(mediaType: SuggestionMediaType): List<RecommendationPagingSource> =
        buildList {
            add(AniListRecommendationSource(mediaType))
            if (mediaType == SuggestionMediaType.ANIME) {
                add(MyAnimeListRecommendationSource(mediaType))
            }
            if (mediaType == SuggestionMediaType.MANGA || mediaType == SuggestionMediaType.NOVEL) {
                if (sourcePreferences.suggestionsUseMangaUpdatesNovel().get()) {
                    add(MangaUpdatesSimilarSource(mediaType))
                }
            }
            if (mediaType == SuggestionMediaType.NOVEL) {
                if (sourcePreferences.suggestionsUseNovelUpdates().get()) {
                    add(NovelUpdatesSimilarSource(mediaType))
                }
            }
        }

    /**
     * Fetch and aggregate suggestions from all applicable sources in parallel.
     *
     * Structured logcat events emitted:
     * - Seed candidates chosen
     * - Provider start/end (delegated to each source)
     * - Coordinator: aggregated result count and failed source count
     * - Cache hit vs miss is logged per-source
     *
     * Deduplication: by providerId (if available) or providerUrl.
     */
    suspend fun fetchSuggestions(
        seed: SuggestionSeed,
        limit: Int = 40,
    ): SuggestionFetchResult = coroutineScope {
        val sources = createSources(seed.mediaType)
        if (sources.isEmpty()) {
            logcat { "[Coordinator] No sources for mediaType=${seed.mediaType}" }
            return@coroutineScope SuggestionFetchResult(emptyList(), 0, 0)
        }

        val translatedTitle = eu.kanade.tachiyomi.data.suggestions.MultilingualQueryHelper.translate(seed.primaryTitle)
        val enrichedCandidates = buildList {
            addAll(seed.candidateTitles)
            if (translatedTitle != null) {
                add(translatedTitle)
            }
        }.distinct()
        val enrichedSeed = seed.copy(candidateTitles = enrichedCandidates)

        logcat {
            "[Coordinator] Fetching '${enrichedSeed.primaryTitle}' (${enrichedSeed.mediaType}) via ${sources.map {
                it.name
            }}" +
                " | candidates=${enrichedSeed.candidateTitles}"
        }

        val jobs = sources.map { source ->
            async(Dispatchers.IO) {
                try {
                    val result = source.fetchSuggestions(enrichedSeed)
                    Pair(result, false)
                } catch (e: Exception) {
                    logcat { "[Coordinator] ${source.name} FAILED: ${e.message}" }
                    Pair(emptyList<SuggestionItem>(), true)
                }
            }
        }

        val results = jobs.map { it.await() }
        val attemptedSources = sources.size
        val failedSources = results.count { it.second }
        val items = results.flatMap { it.first }
            .dedupeByCleanTitle()
            .sortedByDescending { SuggestionSourceWeight.finalScore(it.reason, it.bestMatchScoreFor(enrichedSeed)) }
            .take(limit) // Cap at requested limit

        val matchedBase = sources.any { it.matchedBase } || results.any { it.first.isNotEmpty() }

        logcat {
            "[Coordinator] Done '${enrichedSeed.primaryTitle}': ${items.size} items, " +
                "attempted=$attemptedSources, failed=$failedSources, matchedBase=$matchedBase"
        }

        SuggestionFetchResult(
            items = items,
            attemptedSources = attemptedSources,
            failedSources = failedSources,
            matchedBase = matchedBase,
        )
    }
}
