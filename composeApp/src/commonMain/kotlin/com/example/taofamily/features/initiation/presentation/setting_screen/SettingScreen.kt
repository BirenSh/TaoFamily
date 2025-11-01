package com.example.taofamily.features.initiation.presentation.setting_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.taofamily.core.ui.AppColors
import com.example.taofamily.core.ui.ScreenTopbar
import com.example.taofamily.core.utils.Helper
import com.example.taofamily.features.initiation.presentation.login_screen.LoginScreen

class SettingScreen: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewmodel:SettingViewModel = getScreenModel()
        val onBackPressed: () -> Unit ={
            navigator?.pop()
        }
        val onLogout:()-> Unit = {
            viewmodel.logoutClick()
            navigator?.replaceAll(LoginScreen())
        }
        SettingCompose(
            onBackPressed = onBackPressed,
            onLogout = onLogout
        )

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SettingCompose(onBackPressed: () -> Unit, onLogout: () -> Unit) {
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
          topBar = {
              ScreenTopbar(
                  title = "Setting",
                  onBack = {
                     onBackPressed()
                  },
                  scrollBehavior = scrollBehavior,
                  containerColor = AppColors.TopBarBackground
              )

          }
        ) {
            Box(
                modifier = Modifier.padding(it)
                    .fillMaxSize()
            ){
                Button(
                    onClick = onLogout
                ){
                    Text("Logout")
                }
            }

        }
    }
}