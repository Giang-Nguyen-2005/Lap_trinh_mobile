package com.uth.tuan7trenlop.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val LightPrimary = Color(0xFF2196F3)
val DarkPrimary = Color(0xFF212121)
val PinkPrimary = Color(0xFFEC407A)
val BluePrimary = Color(0xFF64B5F6)

fun colorsForTheme(themeName: String, dark: Boolean = false): ColorScheme {
    return when (themeName) {
        "DARK" -> darkColorScheme(primary = DarkPrimary)
        "PINK" -> lightColorScheme(primary = PinkPrimary)
        "BLUE" -> lightColorScheme(primary = BluePrimary)
        else -> lightColorScheme(primary = LightPrimary)
    }
}
