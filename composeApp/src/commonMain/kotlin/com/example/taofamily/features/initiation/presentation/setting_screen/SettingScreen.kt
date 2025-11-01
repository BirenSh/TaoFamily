package com.example.taofamily.features.initiation.presentation.setting_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.taofamily.core.ui.AppColors
import com.example.taofamily.core.ui.WarningDialog
import com.example.taofamily.core.ui.ScreenTopbar
import com.example.taofamily.features.initiation.domain.language.Language
import com.example.taofamily.features.initiation.presentation.login_screen.LoginScreen
import org.jetbrains.compose.resources.stringResource
import taofamily.composeapp.generated.resources.Res
import taofamily.composeapp.generated.resources.language_label

class SettingScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewmodel: SettingViewModel = getScreenModel()
        val onBackPressed: () -> Unit = {
            navigator?.pop()
        }
        var showWarning by remember {   mutableStateOf(false) }

        val onLogout: () -> Unit = {
            showWarning = true
        }

        val onLanguageSelect: (String) ->Unit = {
            viewmodel.setLanguage(it)
        }

        val currentLang by viewmodel.selectedLanguage.collectAsState()
        val languages = viewmodel.getAvailableLanguages()

        WarningDialog(
          isVisible =  showWarning,
            errorMessage = "Sure Logout? ",
            onDismissCall = {
                showWarning = false
            },
            onActionClick = {
                viewmodel.logoutClick()
                navigator?.replaceAll(LoginScreen())
            }
        )


        SettingCompose(
            onBackPressed = onBackPressed,
            onLogout = onLogout,
            currentLang,
            languages,
            onLanguageSelect
        )

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SettingCompose(
        onBackPressed: () -> Unit,
        onLogout: () -> Unit,
        currentLang: String,
        languages: List<Language>,
        onLanguageSelect: (String) -> Unit
    ) {
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

            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxWidth().fillMaxSize()
                    .padding(horizontal = 10.dp, vertical = 3.dp)
                    .background(AppColors.WhiteSmoke),

                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box() {
                    LanguageSelector(
                        currentLang,
                        languages,
                        onLanguageSelect = {lang->
                            onLanguageSelect(lang)
                        }
                    )
                }
                Box() {
                    Column {
                        ElevatedButton(
                            onClick = onLogout,
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 30.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AppColors.PrimaryOrange,
                                contentColor = Color.Black
                            )

                        ) {
                            Text("Logout Application")
                            Spacer(modifier = Modifier.width(20.dp))
                            Icon(imageVector = Icons.Default.Logout, contentDescription = "logut")

                        }

                        Text(
                            text = "App version: 1.0.0",
                            textAlign = TextAlign.Center,
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }

        }
    }


    @Composable
    fun LanguageSelector(
        currentLang: String,
        languages: List<Language>,
        onLanguageSelect: (String) -> Unit
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(Res.string.language_label),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(10.dp))

            languages.forEach { lang ->
                val isSelected = lang.code == currentLang

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onLanguageSelect(lang.code) }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = { onLanguageSelect(lang.code) }
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = lang.displayName,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }

}