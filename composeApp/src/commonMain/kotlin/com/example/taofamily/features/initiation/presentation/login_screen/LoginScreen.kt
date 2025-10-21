package com.example.taofamily.features.initiation.presentation.login_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import cafe.adriel.voyager.core.screen.Screen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LocalPostOffice
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.taofamily.core.ui.TaoFamilyTheme
import com.example.taofamily.features.initiation.presentation.taochin_screen.MemberListScreen
import org.jetbrains.compose.ui.tooling.preview.Preview


class LoginScreen: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        LoginScreenCompose(
            onLoginSuccess = {
                navigator?.replace(MemberListScreen())
            }
        )
    }

    @Composable
    fun LoginScreenCompose(
        onLoginSuccess: () -> Unit
    ) {

        var partnerKey  by remember { mutableStateOf<String>("re") }
        var userName  by remember { mutableStateOf<String>("sad") }
        var password  by remember { mutableStateOf<String>("dsa") }
        var showValidationError by remember { mutableStateOf(false) }

        val partnerKeyValid = remember(partnerKey) {
            partnerKey.isBlank()
        }
        val userNameValid = remember(userName) {
            userName.isBlank()
        }
        val passwordValid = remember(password) {
            password.isBlank()
        }


        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
                .padding(20.dp)
            ) {
//
            UserTextField(
                value = partnerKey,
                onValueChange = {partnerKey = it},
                label = "Enter your Partner Name ",
                icon = Icons.Default.LocalPostOffice,
                isValid = partnerKeyValid,
                showError = showValidationError

            )
            UserTextField(
                value = userName,
                onValueChange = {userName = it},
                label = "Enter User name",
                icon = Icons.Filled.Person,
                isValid = userNameValid,
                showError = showValidationError
            )
            UserPassword(
                value = password,
                onValueChange = {password = it},
                label = "Enter Password",
                icon = Icons.Filled.Key,
                isValid = passwordValid,
                showError = showValidationError

            )

            Spacer(modifier = Modifier.size(10.dp))

            LoginButton(
                text = "Login",
                onClick = {
                    if (userNameValid || partnerKeyValid || passwordValid){
                        showValidationError = true
                    }
                    else{
                        onLoginSuccess()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

        }
    }


    @Composable
    @Preview(
        showBackground = true,
        widthDp = 360,
        heightDp = 640,
    )
    fun Test(){
        TaoFamilyTheme {
            LoginScreenCompose(onLoginSuccess = {})
        }
    }

}


@Composable
fun UserTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    isValid: Boolean,
    showError: Boolean
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        leadingIcon = { Icon(icon, contentDescription = label) },
        label = {
            Text(label)
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        isError = isValid && showError,
        supportingText = {if (isValid && showError) Text("This Filed is required") else null}

    )
}

@Composable
fun UserPassword(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    isValid: Boolean,
    showError: Boolean
){
    var passwordVisible by remember { mutableStateOf(false) }

    val visualStateTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
    val trailingIcon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {Text(label)},
        leadingIcon = { Icon(imageVector = icon, contentDescription = label) },
        trailingIcon = {
            IconButton(onClick = {passwordVisible = !passwordVisible}){
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = if (passwordVisible) "Hide password" else "Show password"
                )
            }
        },
        visualTransformation = visualStateTransformation,
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
        isError = isValid && showError,
        supportingText = {if (isValid && showError) Text("This Filed is required") else null}
    )
}

@Composable
fun LoginButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = "Login"
){
    Button(
        onClick = onClick,
        modifier = modifier,
    ){
        Text(text, color = Color.Black)
    }
}


