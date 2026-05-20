package eu.kanade.presentation.theme.colorscheme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

internal object SakuraNoirColorScheme : BaseColorScheme() {

    override val darkScheme = darkColorScheme(
        primary = Color(0xFFF6A7C1),
        onPrimary = Color(0xFF150812),
        primaryContainer = Color(0xFF5C2B45),
        onPrimaryContainer = Color(0xFFFFD9E5),
        inversePrimary = Color(0xFFFFC7D8),

        secondary = Color(0xFFFFD166),
        onSecondary = Color(0xFF201100),
        secondaryContainer = Color(0xFF4A3411),
        onSecondaryContainer = Color(0xFFFFE5A1),

        tertiary = Color(0xFF8AF0D5),
        onTertiary = Color(0xFF08211A),
        tertiaryContainer = Color(0xFF1D3D35),
        onTertiaryContainer = Color(0xFFC7FFF0),

        background = Color(0xFF09060B),
        onBackground = Color(0xFFF5EAF0),
        surface = Color(0xFF120C14),
        onSurface = Color(0xFFF5EAF0),
        surfaceVariant = Color(0xFF231625),
        onSurfaceVariant = Color(0xFFE0C6D3),
        surfaceTint = Color(0xFFF6A7C1),
        inverseSurface = Color(0xFFF2EAF0),
        inverseOnSurface = Color(0xFF24161F),

        outline = Color(0xFF7E5E72),
        outlineVariant = Color(0xFF3A2733),

        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        errorContainer = Color(0xFF93000A),
        onErrorContainer = Color(0xFFFFDAD6),

        scrim = Color(0xFF000000),

        surfaceDim = Color(0xFF09060B),
        surfaceBright = Color(0xFF2B1B26),
        surfaceContainerLowest = Color(0xFF050308),
        surfaceContainerLow = Color(0xFF110A13),
        surfaceContainer = Color(0xFF17101A),
        surfaceContainerHigh = Color(0xFF1F1522),
        surfaceContainerHighest = Color(0xFF2A1D2E),
    )

    override val lightScheme = lightColorScheme(
        primary = Color(0xFF8F4A64),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFFFD9E5),
        onPrimaryContainer = Color(0xFF5C2B45),
        inversePrimary = Color(0xFFF6A7C1),

        secondary = Color(0xFF8B6A10),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFFFEFB3),
        onSecondaryContainer = Color(0xFF4D3900),

        tertiary = Color(0xFF126B5B),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFB9F7E7),
        onTertiaryContainer = Color(0xFF004236),

        background = Color(0xFFFFF8FB),
        onBackground = Color(0xFF25161F),
        surface = Color(0xFFFFF8FB),
        onSurface = Color(0xFF25161F),
        surfaceVariant = Color(0xFFF4E6ED),
        onSurfaceVariant = Color(0xFF5C4652),
        surfaceTint = Color(0xFF8F4A64),
        inverseSurface = Color(0xFF382731),
        inverseOnSurface = Color(0xFFFFF8FB),

        outline = Color(0xFF8D6C7C),
        outlineVariant = Color(0xFFE5D4DC),

        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFDAD6),
        onErrorContainer = Color(0xFF410002),

        scrim = Color(0xFF000000),

        surfaceDim = Color(0xFFF2E7EB),
        surfaceBright = Color(0xFFFFFFFF),
        surfaceContainerLowest = Color(0xFFFFFFFF),
        surfaceContainerLow = Color(0xFFFBF0F4),
        surfaceContainer = Color(0xFFF5E8ED),
        surfaceContainerHigh = Color(0xFFEFDEE5),
        surfaceContainerHighest = Color(0xFFE7D3DB),
    )
}
