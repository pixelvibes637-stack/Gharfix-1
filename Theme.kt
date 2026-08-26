package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val GharFixLightColorScheme = lightColorScheme(
    primary = GharFixTeal,
    onPrimary = Color.White,
    primaryContainer = GharFixTealContainer,
    onPrimaryContainer = GharFixTealDark,
    secondary = GharFixNavy,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE0F2FE),
    onSecondaryContainer = Color(0xFF0369A1),
    tertiary = GharFixAmber,
    onTertiary = Color.White,
    tertiaryContainer = GharFixAmberContainer,
    onTertiaryContainer = GharFixAmberDark,
    background = GharFixBackground,
    onBackground = GharFixTextPrimary,
    surface = GharFixSurface,
    onSurface = GharFixTextPrimary,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = GharFixTextSecondary,
    outline = GharFixCardStroke,
    error = GharFixRed,
    onError = Color.White
)

private val GharFixDarkColorScheme = darkColorScheme(
    primary = GharFixTealLight,
    onPrimary = GharFixTealDark,
    primaryContainer = GharFixTeal,
    onPrimaryContainer = Color.White,
    secondary = Color(0xFF7DD3FC),
    onSecondary = Color(0xFF0C4A6E),
    tertiary = GharFixAmber,
    onTertiary = Color(0xFF451A03),
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF475569)
)

@Composable
fun GharFixTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our brand colors for cohesive startup look
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> GharFixDarkColorScheme
        else -> GharFixLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    GharFixTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
