package com.example.taofamily.core.ui


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * This file defines your Material 3 design system, colors, and typography, applied across the entire application via the top-level
 * Define a simple color palette (adjust these to your brand colors)
 */

val PrimaryLight = Color(0xFF6750A4)
val SecondaryLight = Color(0xFF625B71)
// ... define dark colors
val AppBackgroundColor = Color.White

val LightColorScheme = lightColorScheme(
    primary = AppColors.PrimaryOrange,
    secondary = AppColors.PrimaryBlack,
    // ... other light colors
)

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFD0BCFF),
    secondary = Color(0xFFCCC2DC),
    // ... other dark colors
)


@Composable
fun TaoFamilyTheme(
    darkTheme: Boolean = false, // You would detect system dark mode here
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MaterialTheme.typography, // Use default typography for now
        content = content
    )
}