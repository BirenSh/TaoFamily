package com.example.taofamily.features.initiation.presentation.login_screen

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.taofamily.core.platform.checkInternetConnection
import com.example.taofamily.core.utils.UiState
import com.example.taofamily.features.initiation.data.local.SettingPreFrance
import com.example.taofamily.features.initiation.domain.model.LoginModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.log

class LoginViewModel(
    private val settingPreFrance: SettingPreFrance
) : ScreenModel {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _loginResponse = MutableStateFlow<UiState<Boolean>>(UiState.Ideal)
    val loginResponse: StateFlow<UiState<Boolean>> = _loginResponse.asStateFlow()

    fun getAppIsLoggedIn(){
        screenModelScope.launch {
        println("===islogged: ${settingPreFrance.getIsLoggedIn()}")
        _isLoggedIn.value = settingPreFrance.getIsLoggedIn()
        }
    }


    fun loginButtonClick(
        loginModel: LoginModel
    ) {
        screenModelScope.launch {
            _loginResponse.value = UiState.Loading

            if (loginModel.userName.isBlank() || loginModel.password.isBlank() || loginModel.partnerKey.isBlank()) {
                _loginResponse.value = UiState.Error("All fields are required")
            } else if (!checkInternetConnection()) {
                _loginResponse.value = UiState.Error("No internet connection")
            } else {
                settingPreFrance.setIsLoggedIn(true)
                _loginResponse.value = UiState.Success(true)
            }

        }
    }



    fun resetState(){
        _loginResponse.value = UiState.Ideal
    }

}