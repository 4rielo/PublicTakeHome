package com.ascarafia.publictakehome.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun PublicTakeHomeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val lightColorScheme = lightColorScheme(
        primary = Color(0xFF4c662b),
        onPrimary = Color(0xFFffffff),
        primaryContainer = Color(0xFFcdeda3),
        onPrimaryContainer = Color(0xFF354e16),
        inversePrimary = Color(0xFFb1d18a),
        secondary = Color(0xFF586249),
        onSecondary = Color(0xFFffffff),
        secondaryContainer = Color(0xFFdce7c8),
        onSecondaryContainer = Color(0xFF404a33),
        tertiary = Color(0xFF386663),
        onTertiary = Color(0xFFffffff),
        tertiaryContainer = Color(0xFFbcece7),
        onTertiaryContainer = Color(0xFF1f4e4b),
        surface = Color(0xFFf9faef),
        onSurface = Color(0xFF1a1c16),
        surfaceVariant = Color(0xFFdadbd0),
        onSurfaceVariant = Color(0xFF44483d),
        inverseSurface = Color(0xFF2f312a),
        inverseOnSurface = Color(0xFFf1f2e6),
        error = Color(0xFFba1a1a),
        onError = Color(0xFFffffff),
        errorContainer = Color(0xFFffdad6),
        onErrorContainer = Color(0xFF93000a),
        outline = Color(0xFF75796c),
        outlineVariant = Color(0xFFc5c8ba),
        scrim = Color(0xFF000000),
        surfaceBright = Color(0xFFf9faef),
        surfaceContainer = Color(0xFFeeefe3),
        surfaceContainerHigh = Color(0xFFe8e9de),
        surfaceContainerHighest = Color(0xFFe2e3d8),
        surfaceContainerLow = Color(0xFFf3f4e9),
        surfaceContainerLowest = Color(0xFFffffff),
        surfaceDim = Color(0xFFdadbd0),
    )

    val darkColorScheme = darkColorScheme(
        primary = Color(0xFFb1d18a),
        onPrimary = Color(0xFF1f3701),
        primaryContainer = Color(0xFF354e16),
        onPrimaryContainer = Color(0xFFcdeda3),
        inversePrimary = Color(0xFF4c662b),
        secondary = Color(0xFFbfcbad),
        onSecondary = Color(0xFF2a331e),
        secondaryContainer = Color(0xFF404a33),
        onSecondaryContainer = Color(0xFFdce7c8),
        tertiary = Color(0xFFa0d0cb),
        onTertiary = Color(0xFF003735),
        tertiaryContainer = Color(0xFF1f4e4b),
        onTertiaryContainer = Color(0xFFbcece7),
        background = Color(0xFF000000),
        onBackground = Color(0xFFffffff),
        surface = Color(0xFF12140e),
        onSurface = Color(0xFFe2e3d8),
        surfaceVariant = Color(0xFF12140e),
        onSurfaceVariant = Color(0xFFc5c8ba),
        inverseSurface = Color(0xFFe2e3d8),
        inverseOnSurface = Color(0xFF2f312a),
        error = Color(0xFFffb4ab),
        onError = Color(0xFF690005),
        errorContainer = Color(0xFF93000a),
        onErrorContainer = Color(0xFFffdad6),
        outline = Color(0xFF8f9285),
        outlineVariant = Color(0xFF44483d),
        scrim = Color(0xFF000000),
        surfaceBright = Color(0xFF383a32),
        surfaceContainer = Color(0xFF1e201a),
        surfaceContainerHigh = Color(0xFF282b24),
        surfaceContainerHighest = Color(0xFF33362e),
        surfaceContainerLow = Color(0xFF1a1c16),
        surfaceContainerLowest = Color(0xFF0c0f09),
        surfaceDim = Color(0xFF12140e),
    )

    val colors = if (darkTheme) {
        darkColorScheme
    } else {
        lightColorScheme
    }

    MaterialTheme(
        colorScheme = colors,
        typography = MaterialTheme.typography,
        content = content
    )
}