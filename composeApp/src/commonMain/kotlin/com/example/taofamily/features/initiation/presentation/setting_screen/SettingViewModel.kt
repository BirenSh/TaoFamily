package com.example.taofamily.features.initiation.presentation.setting_screen

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.taofamily.features.initiation.data.local.SettingPreFrance
import com.example.taofamily.features.initiation.domain.language.Language
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingViewModel(
    private val settingPreFrance: SettingPreFrance
): ScreenModel {

    private val _selectedLanguage = MutableStateFlow(settingPreFrance.getSavedLanguage() ?: "en")
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

    fun logoutClick(){
        screenModelScope.launch {
        settingPreFrance.setIsLoggedIn(false)
        }
    }

    fun getAvailableLanguages(): List<Language> = listOf(
        Language("en", "English"),
        Language("hi", "हिंदी"),
        Language("zh", "中文")
    )

    fun setLanguage(langCode: String) {
        _selectedLanguage.value = langCode
        screenModelScope.launch {
            settingPreFrance.setLanguage(langCode)
        }
    }
}