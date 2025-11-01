package com.example.taofamily.features.initiation.domain.language

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf


val LocalLanguage = compositionLocalOf { "en" }

@Composable
fun LanguageProvider(
    language: String,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalLanguage provides language) {
        content()
    }
}

@Composable
fun rememberLanguage(): String {
    return LocalLanguage.current
}