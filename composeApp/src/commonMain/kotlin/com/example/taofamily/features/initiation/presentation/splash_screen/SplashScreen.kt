package com.example.taofamily.features.initiation.presentation.splash_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.taofamily.features.initiation.presentation.login_screen.LoginScreen
import com.example.taofamily.features.initiation.presentation.login_screen.LoginViewModel
import com.example.taofamily.features.initiation.presentation.taochin_screen.MemberListScreen
import kotlinx.coroutines.delay

class SplashScreen: Screen {
    @Composable
    override fun Content() {
        val loginViewModel: LoginViewModel = getScreenModel()
        val isLoggedIn  = loginViewModel.isLoggedIn.collectAsState()
        val navigator = LocalNavigator.current
        loginViewModel.getAppIsLoggedIn()

        LaunchedEffect(Unit) {
            delay(2000)
            if (isLoggedIn.value) {
                navigator?.replace(MemberListScreen())
            } else navigator?.replace(LoginScreen())
        }

        SplashCompose()

    }

    @Composable
    fun SplashCompose() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F8FA)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "home",
                modifier = Modifier.size(40.dp)
            )
        }
    }
}