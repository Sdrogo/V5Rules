package com.example.v5rules.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.graphics.ColorUtils
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color.White,
    secondary = Color(0XFFfefbf1),
    tertiary = Color(0XFF76031A),
    background = Color(0xFF222222),
    surface = Color(0xFF222222),
    onPrimary = Color(0xFF4d0211),
    onSecondary = Color(0XFFfefbf1),
    onTertiary = Color(0xFF262926),
    onBackground = Color.White,
    onSurface = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1c1c1c),
    secondary = Color.White,
    tertiary = Color(0XFF76031A),
    background = Color(0XFFfefbf1),
    surface = Color(0XFFfefbf1),
    onPrimary = Color(0xFF4d0211),
    onSecondary = Color(0XFFfefbf1),
    onTertiary = Color(0xFF231f20),
    onBackground = Color(0xFF1c1c1c),
    onSurface = Color(0xFF1c1c1c),
)

@Composable
fun V5RulesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Disable dynamic colors
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val isLightStatusBar =
                ColorUtils.calculateLuminance(colorScheme.tertiary.toArgb()) > 0.5
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                isLightStatusBar
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                isLightStatusBar
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}