package com.sparkathon.shopmate.ui.theme

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

// Walmart Color Palette
private val WalmartBlue = Color(0xFF0071CE)
private val WalmartDarkBlue = Color(0xFF003087)
private val WalmartYellow = Color(0xFFFFCC00)
private val WalmartLightGray = Color(0xFFE0E0E0)
private val WalmartGray = Color(0xFF75787B)

private val DarkColorScheme = darkColorScheme(
    primary = WalmartBlue,
    secondary = WalmartDarkBlue,
    tertiary = WalmartYellow,
    background = Color.Black,
    surface = WalmartGray,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary = WalmartBlue,
    secondary = WalmartDarkBlue,
    tertiary = WalmartYellow,
    background = Color.White,
    surface = WalmartLightGray,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
)

@Composable
fun ShopMateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
