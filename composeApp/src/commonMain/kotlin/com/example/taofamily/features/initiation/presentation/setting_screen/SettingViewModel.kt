package com.example.taofamily.features.initiation.presentation.setting_screen

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.taofamily.features.initiation.data.local.SettingPreFrance
import kotlinx.coroutines.launch

class SettingViewModel(
    private val settingPreFrance: SettingPreFrance
): ScreenModel {

    fun logoutClick(){
        screenModelScope.launch {
        settingPreFrance.setIsLoggedIn(false)
        }
    }
}