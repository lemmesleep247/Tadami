package eu.kanade.presentation.theme.colorscheme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

internal object NebulaTideColorScheme : BaseColorScheme() {

    override val darkScheme = darkColorScheme(
        primary = Color(0xFF6EF6FF),
        onPrimary = Color(0xFF07111F),
        primaryContainer = Color(0xFF1D5160),
        onPrimaryContainer = Color(0xFFCEF9FF),
        inversePrimary = Color(0xFF2A86A0),

        secondary = Color(0xFF9B8CFF),
        onSecondary = Color(0xFF090A17),
        secondaryContainer = Color(0xFF352E78),
        onSecondaryContainer = Color(0xFFE4DEFF),

        tertiary = Color(0xFF79F2B5),
        onTertiary = Color(0xFF032116),
        tertiaryContainer = Color(0xFF184339),
        onTertiaryContainer = Color(0xFFC9FFE6),

        background = Color(0xFF07111F),
        onBackground = Color(0xFFE8F3FF),
        surface = Color(0xFF0B1528),
        onSurface = Color(0xFFE8F3FF),
        surfaceVariant = Color(0xFF16263A),
        onSurfaceVariant = Color(0xFFCBD8EA),
        surfaceTint = Color(0xFF6EF6FF),
        inverseSurface = Color(0xFFEAF3FF),
        inverseOnSurface = Color(0xFF08111E),

        outline = Color(0xFF556A80),
        outlineVariant = Color(0xFF223247),

        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        errorContainer = Color(0xFF93000A),
        onErrorContainer = Color(0xFFFFDAD6),

        scrim = Color(0xFF000000),

        surfaceDim = Color(0xFF07111F),
        surfaceBright = Color(0xFF20304A),
        surfaceContainerLowest = Color(0xFF050B14),
        surfaceContainerLow = Color(0xFF0B1423),
        surfaceContainer = Color(0xFF111B2C),
        surfaceContainerHigh = Color(0xFF172338),
        surfaceContainerHighest = Color(0xFF20304A),
    )

    override val lightScheme = lightColorScheme(
        primary = Color(0xFF116A7A),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFC9F7FF),
        onPrimaryContainer = Color(0xFF004955),
        inversePrimary = Color(0xFF6EF6FF),

        secondary = Color(0xFF5148A7),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFE4DEFF),
        onSecondaryContainer = Color(0xFF2C246E),

        tertiary = Color(0xFF137A58),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFC9FFE6),
        onTertiaryContainer = Color(0xFF00482F),

        background = Color(0xFFF6FBFF),
        onBackground = Color(0xFF172231),
        surface = Color(0xFFF6FBFF),
        onSurface = Color(0xFF172231),
        surfaceVariant = Color(0xFFE4EDF7),
        onSurfaceVariant = Color(0xFF4A5B70),
        surfaceTint = Color(0xFF116A7A),
        inverseSurface = Color(0xFF233144),
        inverseOnSurface = Color(0xFFF6FBFF),

        outline = Color(0xFF6E8096),
        outlineVariant = Color(0xFFD7E1ED),

        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFDAD6),
        onErrorContainer = Color(0xFF410002),

        scrim = Color(0xFF000000),

        surfaceDim = Color(0xFFE6EDF6),
        surfaceBright = Color(0xFFFFFFFF),
        surfaceContainerLowest = Color(0xFFFFFFFF),
        surfaceContainerLow = Color(0xFFF2F7FC),
        surfaceContainer = Color(0xFFE9F0F8),
        surfaceContainerHigh = Color(0xFFE0E9F2),
        surfaceContainerHighest = Color(0xFFD4DFEA),
    )
}
