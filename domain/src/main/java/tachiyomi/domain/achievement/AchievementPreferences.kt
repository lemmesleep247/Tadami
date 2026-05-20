package tachiyomi.domain.achievement

import tachiyomi.core.common.preference.PreferenceStore

class AchievementPreferences(
    private val preferenceStore: PreferenceStore,
) {
    fun unlockedAchievements() = preferenceStore.getStringSet("unlocked_achievements", emptySet())

    fun unlockedThemes() = preferenceStore.getStringSet("unlocked_themes", emptySet())

    fun unlockedAuras() = preferenceStore.getStringSet("unlocked_auras", emptySet())

    fun activeAura() = preferenceStore.getString("active_aura", "")

    fun isTreasuryUnlocked() = preferenceStore.getBoolean("is_treasury_unlocked", false)
}
