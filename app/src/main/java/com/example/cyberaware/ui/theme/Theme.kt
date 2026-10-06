package com.example.cyberaware.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = CyberCyanPrimary,
    onPrimary = Color(0xFF00364A),
    primaryContainer = CyberDarkSurfaceVariant,
    onPrimaryContainer = CyberCyanLight,
    secondary = CyberBlueSecondary,
    onSecondary = Color.White,
    background = CyberDarkBackground,
    onBackground = CyberTextPrimary,
    surface = CyberDarkSurface,
    onSurface = CyberTextPrimary,
    surfaceVariant = CyberDarkSurfaceVariant,
    onSurfaceVariant = CyberTextSecondary,
    outline = CyberCardBorder,
    error = CyberError,
    onError = Color.White,
)

@Composable
fun CyberAwareTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    // Default to dark cybersecurity scheme for optimal aesthetic
    val colorScheme = DarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            window?.let { win ->
                @Suppress("DEPRECATION")
                win.statusBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(win, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
