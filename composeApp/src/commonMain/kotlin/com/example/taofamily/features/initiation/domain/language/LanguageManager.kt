package com.example.taofamily.features.initiation.domain.language

import com.example.taofamily.core.platform.applyLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object LanguageManager {
    private val _currentLanguage = MutableStateFlow("en")
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    fun setLanguage(langCode: String) {
        _currentLanguage.value = langCode
        applyLanguage(langCode)
    }

    fun getCurrentLanguage(): String = _currentLanguage.value
}

