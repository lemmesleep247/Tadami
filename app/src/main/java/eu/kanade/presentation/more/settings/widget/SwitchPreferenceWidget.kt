package eu.kanade.presentation.more.settings.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewLightDark
import eu.kanade.presentation.more.resolveAuroraMoreCheckedTrackColor
import eu.kanade.presentation.more.settings.LocalSettingsUiStyle
import eu.kanade.presentation.more.settings.SettingsUiStyle
import eu.kanade.presentation.more.settings.settingsAccentColor
import eu.kanade.presentation.theme.AuroraTheme
import eu.kanade.presentation.theme.TachiyomiPreviewTheme

@Composable
fun SwitchPreferenceWidget(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    icon: ImageVector? = null,
    checked: Boolean = false,
    enabled: Boolean = true,
    onCheckedChanged: (Boolean) -> Unit,
) {
    val isAurora = LocalSettingsUiStyle.current == SettingsUiStyle.Aurora
    val colors = AuroraTheme.colors
    val accent = settingsAccentColor()
    TextPreferenceWidget(
        modifier = modifier.alpha(if (enabled) 1f else 0.6f),
        title = title,
        subtitle = subtitle,
        icon = icon,
        widget = {
            Switch(
                checked = checked,
                enabled = enabled,
                onCheckedChange = null,
                colors = if (isAurora) {
                    SwitchDefaults.colors(
                        checkedThumbColor = accent,
                        checkedTrackColor = resolveAuroraMoreCheckedTrackColor(colors),
                        uncheckedThumbColor = if (colors.isDark || colors.isEInk) {
                            colors.textPrimary
                        } else {
                            Color.White
                        },
                        uncheckedTrackColor = if (colors.isDark || colors.isEInk) {
                            colors.textSecondary.copy(alpha = 0.32f)
                        } else {
                            Color(0xFFD9DEE8)
                        },
                        uncheckedBorderColor = if (colors.isDark || colors.isEInk) {
                            colors.textSecondary.copy(alpha = 0.42f)
                        } else {
                            Color(0xFFBCC6D6)
                        },
                        disabledUncheckedThumbColor = if (colors.isDark || colors.isEInk) {
                            colors.textPrimary.copy(alpha = 0.55f)
                        } else {
                            Color.White.copy(alpha = 0.92f)
                        },
                        disabledUncheckedTrackColor = if (colors.isDark || colors.isEInk) {
                            colors.textSecondary.copy(alpha = 0.18f)
                        } else {
                            Color(0xFFD9DEE8).copy(alpha = 0.68f)
                        },
                        disabledUncheckedBorderColor = if (colors.isDark || colors.isEInk) {
                            colors.textSecondary.copy(alpha = 0.24f)
                        } else {
                            Color(0xFFBCC6D6).copy(alpha = 0.72f)
                        },
                    )
                } else {
                    SwitchDefaults.colors()
                },
                modifier = Modifier.padding(start = TrailingWidgetBuffer),
            )
        },
        onPreferenceClick = if (enabled) {
            { onCheckedChanged(!checked) }
        } else {
            null
        },
    )
}

@PreviewLightDark
@Composable
private fun SwitchPreferenceWidgetPreview() {
    TachiyomiPreviewTheme {
        Surface {
            Column {
                SwitchPreferenceWidget(
                    title = "Text preference with icon",
                    subtitle = "Text preference summary",
                    icon = Icons.Filled.Preview,
                    checked = true,
                    onCheckedChanged = {},
                )
                SwitchPreferenceWidget(
                    title = "Text preference",
                    subtitle = "Text preference summary",
                    checked = false,
                    onCheckedChanged = {},
                )
                SwitchPreferenceWidget(
                    title = "Text preference no summary",
                    checked = false,
                    onCheckedChanged = {},
                )
                SwitchPreferenceWidget(
                    title = "Another text preference no summary",
                    checked = false,
                    onCheckedChanged = {},
                )
            }
        }
    }
}
