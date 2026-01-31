package com.analystlab.app.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

private val lightThemeColors = lightColorScheme(
    primary = PrimaryBlue,
    primaryContainer = PrimaryBlueLight,
    onPrimary = Color.White,
    onPrimaryContainer = PrimaryBlueDark,
    secondary = AccentPurple,
    secondaryContainer = AccentPurpleLight,
    onSecondary = Color.White,
    error = ErrorRed,
    errorContainer = ErrorRedLight,
    onError = Color.White,
    background = BackgroundGray,
    onBackground = TextPrimary,
    surface = SurfaceWhite,
    onSurface = TextPrimary,
    surfaceVariant = BackgroundBlue,
    onSurfaceVariant = TextSecondary,
    outline = BorderLight
)

private val darkThemeColors = darkColorScheme(
    primary = PrimaryBlueLight,
    primaryContainer = PrimaryBlueDark,
    onPrimary = Color.Black,
    onPrimaryContainer = PrimaryBlueLight,
    secondary = AccentPurple,
    secondaryContainer = AccentPurpleLight,
    onSecondary = Color.Black,
    error = ErrorRed,
    errorContainer = ErrorRedLight,
    onError = Color.White,
    background = Color(0xFF0F172A),
    onBackground = Color.White,
    surface = SurfaceDark,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = TextTertiary,
    outline = BorderDark
)

@Composable
fun AnalystLabTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> darkThemeColors
        else -> lightThemeColors
    }
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            content()
        }
    }
}
