package com.example.taofamily

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.navigator.Navigator
import com.example.taofamily.core.ui.TaoFamilyTheme
import com.example.taofamily.features.initiation.data.local.SettingPreFrance
import com.example.taofamily.features.initiation.domain.language.LanguageManager
import com.example.taofamily.features.initiation.domain.language.LanguageProvider
import com.example.taofamily.features.initiation.presentation.login_screen.LoginScreen
import com.example.taofamily.features.initiation.presentation.splash_screen.SplashScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.*
import org.koin.compose.koinInject




@Composable
@Preview
fun App() {
    val settingPreFrance: SettingPreFrance = koinInject()

    // Load saved language on app start
    LaunchedEffect(Unit) {
        val savedLang = settingPreFrance.getSavedLanguage() ?: "en"
        LanguageManager.setLanguage(savedLang)
    }

    // Observe language changes
    val currentLanguage by LanguageManager.currentLanguage.collectAsState()


    LanguageProvider(language = currentLanguage) {
        TaoFamilyTheme {
            Navigator(screen = SplashScreen())
        }
    }
}