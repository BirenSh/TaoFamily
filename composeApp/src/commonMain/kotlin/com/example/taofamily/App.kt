package com.example.taofamily

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.example.taofamily.core.ui.TaoFamilyTheme
import com.example.taofamily.features.initiation.presentation.login_screen.LoginScreen
import com.example.taofamily.features.initiation.presentation.splash_screen.SplashScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    TaoFamilyTheme {
        Navigator(
            screen = SplashScreen() // Starts the navigation stack at LoginScreen
        )
    }
}