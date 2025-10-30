package com.example.taofamily.features.initiation.presentation.login_screen

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.taofamily.features.initiation.data.local.SettingPreFrance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val settingPreFrance: SettingPreFrance
) : ScreenModel {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    fun setIsLoggedIn(value: Boolean) {
        screenModelScope.launch {
            settingPreFrance.setIsLoggedIn(value)
            _isLoggedIn.value = true
        }
    }

    fun getAppIsLoggedIn(){
        println("===islogged: ${settingPreFrance.getIsLoggedIn()}")
        _isLoggedIn.value = settingPreFrance.getIsLoggedIn()


    }
}