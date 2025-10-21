package com.example.taofamily

import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import com.example.taofamily.core.ui.TaoFamilyTheme
import com.example.taofamily.features.initiation.presentation.detail_screen.MemberDetailScreen
import com.example.taofamily.features.initiation.presentation.filter_screen.FilterScreen
import com.example.taofamily.features.initiation.presentation.form_screen.InitiationFormScreen
import com.example.taofamily.features.initiation.presentation.login_screen.LoginScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {

    TaoFamilyTheme {
        Navigator(
            screen = LoginScreen() // Starts the navigation stack at LoginScreen
        )
    }
}