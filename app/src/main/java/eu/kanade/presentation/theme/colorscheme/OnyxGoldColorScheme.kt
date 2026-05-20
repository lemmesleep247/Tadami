package eu.kanade.presentation.theme.colorscheme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/**
 * Colors for Onyx Gold theme
 * A premium dark gold theme inspired by luxury onyx stone and polished gold accents.
 */
internal object OnyxGoldColorScheme : BaseColorScheme() {

    override val darkScheme = darkColorScheme(
        // Primary colors (Gold)
        primary = Color(0xFFD4AF37), // Metallic Gold
        onPrimary = Color(0xFF050507), // Deep Onyx
        primaryContainer = Color(0xFF8C7323),
        onPrimaryContainer = Color(0xFFFFF0B8),
        inversePrimary = Color(0xFF1E1E24),

        // Secondary colors
        secondary = Color(0xFFFFD700), // Pure Gold for unread badges and highlights
        onSecondary = Color(0xFF050507),
        secondaryContainer = Color(0xFF3A3114), // Muted dark gold for nav pills/progress
        onSecondaryContainer = Color(0xFFFFEFA8),

        // Tertiary colors (Bronze/Champagne)
        tertiary = Color(0xFFF5F5DC), // Beige/Pearl
        onTertiary = Color(0xFF050507),
        tertiaryContainer = Color(0xFF4D4D3D),
        onTertiaryContainer = Color(0xFFFAFAD2),

        // Background & Surface (Onyx Black)
        background = Color(0xFF050507), // Pure Onyx Black
        onBackground = Color(0xFFECE5C8), // Light warm gold text
        surface = Color(0xFF0A0A0E), // Slate Onyx
        onSurface = Color(0xFFECE5C8),
        surfaceVariant = Color(0xFF111116), // Dark slate
        onSurfaceVariant = Color(0xFFD4CFA9),
        surfaceTint = Color(0xFFD4AF37),
        inverseSurface = Color(0xFFEBEBEB),
        inverseOnSurface = Color(0xFF111116),

        // Outline
        outline = Color(0xFFD4AF37),
        outlineVariant = Color(0xFF554418),

        // Error
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        errorContainer = Color(0xFF93000A),
        onErrorContainer = Color(0xFFFFDAD6),

        // Scrim
        scrim = Color(0xFF000000),

        // Surface containers
        surfaceDim = Color(0xFF050507),
        surfaceBright = Color(0xFF1F1F24),
        surfaceContainerLowest = Color(0xFF020203),
        surfaceContainerLow = Color(0xFF0B0B0F),
        surfaceContainer = Color(0xFF101015), // Navigation bar background
        surfaceContainerHigh = Color(0xFF16161B),
        surfaceContainerHighest = Color(0xFF1D1D23),
    )

    override val lightScheme = lightColorScheme(
        // Primary colors (Warm Gold/Bronze)
        primary = Color(0xFF8C7323),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFF3E5AB),
        onPrimaryContainer = Color(0xFF554418),
        inversePrimary = Color(0xFFD4AF37),

        // Secondary colors
        secondary = Color(0xFF8C7323),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFFFF7D6),
        onSecondaryContainer = Color(0xFF3A3008),

        // Tertiary colors (Pearl/Slate)
        tertiary = Color(0xFF4A4A3A),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFD2D2C2),
        onTertiaryContainer = Color(0xFF1C1C12),

        // Background & Surface (Alabaster / Warm White)
        background = Color(0xFFFAF9F6),
        onBackground = Color(0xFF1C1B12),
        surface = Color(0xFFFAF9F6),
        onSurface = Color(0xFF1C1B12),
        surfaceVariant = Color(0xFFF5F3E9),
        onSurfaceVariant = Color(0xFF4E472A),
        surfaceTint = Color(0xFF8C7323),
        inverseSurface = Color(0xFF2C2B22),
        inverseOnSurface = Color(0xFFFAF9F6),

        // Outline
        outline = Color(0xFF8C7323),
        outlineVariant = Color(0xFFDFDAC9),

        // Error
        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFDAD6),
        onErrorContainer = Color(0xFF410002),

        // Scrim
        scrim = Color(0xFF000000),

        // Surface containers
        surfaceDim = Color(0xFFEBEAE2),
        surfaceBright = Color(0xFFFFFFFF),
        surfaceContainerLowest = Color(0xFFFFFFFF),
        surfaceContainerLow = Color(0xFFF6F5ED),
        surfaceContainer = Color(0xFFEFEFE5),
        surfaceContainerHigh = Color(0xFFE8E8DD),
        surfaceContainerHighest = Color(0xFFDFDFD2),
    )
}
