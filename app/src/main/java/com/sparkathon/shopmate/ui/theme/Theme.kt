package com.sparkathon.shopmate.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Walmart Color Palette
private val WalmartBlue = Color(0xFF0071CE)
private val WalmartDarkBlue = Color(0xFF003087)
private val WalmartYellow = Color(0xFFFFCC00)
private val WalmartLightGray = Color(0xFFE0E0E0)
private val WalmartGray = Color(0xFF75787B)

private val ColorScheme = lightColorScheme(
    primary = WalmartBlue,
    secondary = WalmartDarkBlue,
    tertiary = WalmartYellow,
    background = WalmartLightGray,
    surface = WalmartGray,
    onPrimary = WalmartLightGray,
    onSecondary = WalmartLightGray,
    onTertiary = WalmartLightGray,
    onBackground = WalmartGray,
    onSurface = WalmartLightGray,
)

@Composable
fun ShopMateTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ColorScheme,
        typography = Typography,
        content = content
    )
}
