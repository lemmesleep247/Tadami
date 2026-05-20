package eu.kanade.presentation.more.onboarding

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import eu.kanade.domain.ui.UiPreferences
import eu.kanade.presentation.theme.AuroraSurfaceLevel
import eu.kanade.presentation.theme.AuroraTheme
import eu.kanade.presentation.theme.auroraFloatingSurface
import eu.kanade.presentation.theme.resolveAuroraBorderColor
import eu.kanade.presentation.theme.resolveAuroraSelectionBorderColor
import eu.kanade.presentation.theme.resolveAuroraSelectionContainerColor
import eu.kanade.presentation.theme.resolveAuroraSurfaceColor
import tachiyomi.i18n.MR
import tachiyomi.presentation.core.components.material.padding
import tachiyomi.presentation.core.i18n.stringResource
import tachiyomi.presentation.core.util.collectAsState
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

internal class MediaModesStep : OnboardingStep {

    private val uiPreferences: UiPreferences = Injekt.get()

    private var _isComplete by mutableStateOf(true)

    override val isComplete: Boolean
        get() = _isComplete

    @Composable
    override fun Content() {
        val showAnimePref = uiPreferences.showAnimeSection()
        val showMangaPref = uiPreferences.showMangaSection()
        val showNovelPref = uiPreferences.showNovelSection()

        val showAnime by showAnimePref.collectAsState()
        val showManga by showMangaPref.collectAsState()
        val showNovel by showNovelPref.collectAsState()

        val colors = AuroraTheme.colors

        // Enforce that at least one is complete
        LaunchedEffect(showAnime, showManga, showNovel) {
            _isComplete = showAnime || showManga || showNovel
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = stringResource(MR.strings.onboarding_media_modes_title),
                style = MaterialTheme.typography.headlineSmall,
                color = colors.textPrimary,
            )

            Text(
                text = stringResource(MR.strings.onboarding_media_modes_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = colors.textSecondary,
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                MediaModeCard(
                    title = stringResource(MR.strings.onboarding_media_modes_anime),
                    subtitle = stringResource(MR.strings.onboarding_media_modes_anime_subtitle),
                    icon = Icons.Default.PlayCircle,
                    selected = showAnime,
                    onClick = {
                        val nextVal = !showAnime
                        if (nextVal || showManga || showNovel) {
                            showAnimePref.set(nextVal)
                        }
                    },
                )

                MediaModeCard(
                    title = stringResource(MR.strings.onboarding_media_modes_manga),
                    subtitle = stringResource(MR.strings.onboarding_media_modes_manga_subtitle),
                    icon = Icons.Default.Book,
                    selected = showManga,
                    onClick = {
                        val nextVal = !showManga
                        if (showAnime || nextVal || showNovel) {
                            showMangaPref.set(nextVal)
                        }
                    },
                )

                MediaModeCard(
                    title = stringResource(MR.strings.onboarding_media_modes_novels),
                    subtitle = stringResource(MR.strings.onboarding_media_modes_novels_subtitle),
                    icon = Icons.AutoMirrored.Filled.MenuBook,
                    selected = showNovel,
                    onClick = {
                        val nextVal = !showNovel
                        if (showAnime || showManga || nextVal) {
                            showNovelPref.set(nextVal)
                        }
                    },
                )
            }

            if (!_isComplete) {
                Text(
                    text = stringResource(MR.strings.onboarding_media_modes_error),
                    color = colors.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }

    @Composable
    private fun MediaModeCard(
        title: String,
        subtitle: String,
        icon: ImageVector,
        selected: Boolean,
        onClick: () -> Unit,
    ) {
        val colors = AuroraTheme.colors
        val baseBg = resolveAuroraSurfaceColor(colors, AuroraSurfaceLevel.Glass)
        val selectedBg = resolveAuroraSelectionContainerColor(colors)
        val bgAnim by animateColorAsState(targetValue = if (selected) selectedBg else baseBg, label = "cardBg")

        val baseBorder = resolveAuroraBorderColor(colors, false)
        val selectedBorder = resolveAuroraSelectionBorderColor(colors)
        val borderAnim by animateColorAsState(
            targetValue = if (selected) selectedBorder else baseBorder,
            label = "cardBorder",
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .auroraFloatingSurface(colors, AuroraSurfaceLevel.Glass, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(bgAnim)
                .border(1.dp, borderAnim, RoundedCornerShape(16.dp))
                .clickable { onClick() }
                .padding(16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (selected) colors.accent.copy(alpha = 0.2f) else colors.textPrimary.copy(alpha = 0.05f),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (selected) colors.accent else colors.textSecondary,
                        modifier = Modifier.size(28.dp),
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = colors.textPrimary,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.textSecondary,
                    )
                }

                Checkbox(
                    checked = selected,
                    onCheckedChange = { onClick() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = colors.accent,
                        uncheckedColor = colors.textSecondary,
                        checkmarkColor = colors.textOnAccent,
                    ),
                )
            }
        }
    }
}
