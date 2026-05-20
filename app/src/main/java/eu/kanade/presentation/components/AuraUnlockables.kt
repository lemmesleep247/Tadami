@file:Suppress("ktlint:standard:filename")

package eu.kanade.presentation.components

import androidx.compose.ui.graphics.Color

internal data class AuraPalette(
    val id: String,
    val title: String,
    val description: String,
    val accentColor: Color,
    val gradientColors: List<Color>,
)

private val auraPalettes = listOf(
    AuraPalette(
        id = "aura_harem",
        title = "Гаремная аура",
        description = "Розовое свечение вокруг карточек контента",
        accentColor = Color(0xFFFF69B4),
        gradientColors = listOf(Color(0xFFFFB7D5), Color(0xFFFF69B4), Color(0xFFFF1493)),
    ),
    AuraPalette(
        id = "aura_matrix",
        title = "Цифровой дождь",
        description = "Неоновое зеленое свечение с цифровым настроением",
        accentColor = Color(0xFF00FF41),
        gradientColors = listOf(Color(0xFF00FF41), Color(0xFF008F11), Color(0xFF00FF41)),
    ),
    AuraPalette(
        id = "aura_level_up",
        title = "Аура мастера достижений",
        description = "Золотое сияние за прогресс по достижениям",
        accentColor = Color(0xFFFFD700),
        gradientColors = listOf(Color(0xFFFFE082), Color(0xFFFFD700), Color(0xFFFFB300)),
    ),
)

private val auraPriority = listOf(
    "aura_harem",
    "aura_matrix",
    "aura_level_up",
)

internal fun resolveAuraPalette(id: String): AuraPalette? {
    return auraPalettes.firstOrNull { it.id == id }
}

internal fun resolveActiveAuraPalette(enabledAuras: Set<String>): AuraPalette? {
    val activeId = auraPriority.firstOrNull(enabledAuras::contains) ?: return null
    return resolveAuraPalette(activeId)
}

internal fun allAuraPalettes(): List<AuraPalette> = auraPalettes
