package eu.kanade.presentation.more.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import eu.kanade.presentation.theme.AuroraSurfaceLevel
import eu.kanade.presentation.theme.AuroraTheme
import eu.kanade.presentation.theme.TachiyomiPreviewTheme
import eu.kanade.presentation.theme.auroraFloatingSurface
import eu.kanade.presentation.theme.resolveAuroraBorderColor
import eu.kanade.presentation.theme.resolveAuroraSurfaceColor
import tachiyomi.i18n.MR
import tachiyomi.presentation.core.components.material.padding
import tachiyomi.presentation.core.i18n.stringResource

internal class GuidesStep(
    private val onRestoreBackup: () -> Unit,
) : OnboardingStep {

    override val isComplete: Boolean = true

    @Composable
    override fun Content() {
        val handler = LocalUriHandler.current
        val colors = AuroraTheme.colors

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Elegant Success Card with Glow
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .auroraFloatingSurface(colors, AuroraSurfaceLevel.Glass, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .background(resolveAuroraSurfaceColor(colors, AuroraSurfaceLevel.Glass))
                    .border(1.dp, resolveAuroraBorderColor(colors, true), RoundedCornerShape(20.dp))
                    .padding(20.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(colors.success.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Celebration,
                            contentDescription = null,
                            tint = colors.success,
                            modifier = Modifier.size(36.dp),
                        )
                    }

                    Text(
                        text = stringResource(MR.strings.onboarding_guides_all_set),
                        style = MaterialTheme.typography.titleLarge,
                        color = colors.textPrimary,
                    )

                    Text(
                        text = stringResource(MR.strings.onboarding_guides_welcome_desc),
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.textSecondary,
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(MR.strings.onboarding_guides_next_steps),
                style = MaterialTheme.typography.titleMedium,
                color = colors.textPrimary,
            )

            // Dynamic Action Cards
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                // New User Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .auroraFloatingSurface(colors, AuroraSurfaceLevel.Glass, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(resolveAuroraSurfaceColor(colors, AuroraSurfaceLevel.Glass))
                        .border(1.dp, resolveAuroraBorderColor(colors, false), RoundedCornerShape(16.dp))
                        .padding(16.dp),
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.HelpOutline,
                                contentDescription = null,
                                tint = colors.accent,
                                modifier = Modifier.size(24.dp),
                            )
                            Text(
                                text = stringResource(
                                    MR.strings.onboarding_guides_new_user,
                                    stringResource(MR.strings.app_name),
                                ),
                                style = MaterialTheme.typography.bodyMedium,
                                color = colors.textPrimary,
                            )
                        }

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { handler.openUri(GETTING_STARTED_URL) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colors.accent,
                                contentColor = colors.textOnAccent,
                            ),
                            shape = RoundedCornerShape(12.dp),
                        ) {
                            Text(stringResource(MR.strings.getting_started_guide))
                        }
                    }
                }

                // Returning User Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .auroraFloatingSurface(colors, AuroraSurfaceLevel.Glass, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(resolveAuroraSurfaceColor(colors, AuroraSurfaceLevel.Glass))
                        .border(1.dp, resolveAuroraBorderColor(colors, false), RoundedCornerShape(16.dp))
                        .padding(16.dp),
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Restore,
                                contentDescription = null,
                                tint = colors.accent,
                                modifier = Modifier.size(24.dp),
                            )
                            Text(
                                text = stringResource(
                                    MR.strings.onboarding_guides_returning_user,
                                    stringResource(MR.strings.app_name),
                                ),
                                style = MaterialTheme.typography.bodyMedium,
                                color = colors.textPrimary,
                            )
                        }

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = onRestoreBackup,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colors.accent.copy(alpha = 0.15f),
                                contentColor = colors.accent,
                            ),
                            shape = RoundedCornerShape(12.dp),
                        ) {
                            Text(stringResource(MR.strings.pref_restore_backup))
                        }
                    }
                }
            }
        }
    }
}

const val GETTING_STARTED_URL = "https://aniyomi.org/docs/guides/getting-started"

@PreviewLightDark
@Composable
private fun GuidesStepPreview() {
    TachiyomiPreviewTheme {
        GuidesStep(
            onRestoreBackup = {},
        ).Content()
    }
}
