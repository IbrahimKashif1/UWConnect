package com.uwconnect.android.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.RemoveRedEye
import androidx.compose.material.icons.rounded.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.uwconnect.android.presentation.viewmodel.LoginViewModel
import com.uwconnect.android.presentation.ui.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.uwconnect.android.domain.model.AccountType
import com.uwconnect.android.presentation.components.AccountTypeToggle
import com.uwconnect.android.presentation.components.AuthenticationButton
import com.uwconnect.android.presentation.components.NavigationHelper
import com.uwconnect.android.presentation.components.TextEntryModule
import kotlin.reflect.KSuspendFunction0

@Composable
fun LoginScreen(
    onLoginSuccessNavigation: (String) -> Unit,
    onNavigateToSignupScreen: () -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val userType by loginViewModel.userType.collectAsState()

    NavigationHelper(
        shouldNavigate = {
            loginViewModel.loginState.isLoginSuccess
        },
        destination = {
            onLoginSuccessNavigation(userType!!)
        }
    )

    LaunchedEffect(Unit) {

    }

    Box(modifier = Modifier.fillMaxSize().background(DarkBlue)) {
        Box(
            modifier = Modifier.fillMaxSize().height(120.dp).padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(text = "Login",
                color = White,
                fontSize = 50.sp,
                fontFamily = PoppinsFamily,
                fontWeight = FontWeight.SemiBold,
                fontStyle = FontStyle.Normal
            )
        }
        LoginContainer(
            emailValue = { loginViewModel.loginState.email },
            passwordValue = { loginViewModel.loginState.password },
            buttonEnabled = { loginViewModel.loginState.isInputValid },
            onEmailChanged = loginViewModel::onEmailChanged,
            onPasswordChanged = loginViewModel::onPasswordChanged,
            onLoginButtonClick = loginViewModel::onLoginClick,
            onAccountTypeSelected = loginViewModel::onAccountTypeChanged,
            isPasswordShown = { loginViewModel.loginState.isPasswordShown },
            onTrailingIconClick = loginViewModel::onPasswordVisibilityToggle,
            errorHint = {
                if (loginViewModel.loginState.errorMessageInput != null) {
                    loginViewModel.loginState.errorMessageInput
                } else if (loginViewModel.loginState.errorMessageLogin != null) {
                    loginViewModel.loginState.errorMessageLogin
                } else {
                    null
                }
            },
            isLoading = { loginViewModel.loginState.isLoading },
            modifier = Modifier.padding(16.dp).fillMaxWidth(.9f)
                .background(DarkBlue).padding(10.dp, 15.dp, 10.dp, 5.dp)
                .align(Alignment.Center)
        )
        Row(
            modifier = Modifier.padding(bottom = 10.dp).align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                "No account yet?",
                fontWeight = FontWeight.Light,
                fontSize = 14.sp,
                fontFamily = PoppinsFamily,
                color = White
            )
            Text(
                "Sign Up!",
                modifier = Modifier.padding(start = 5.dp).clickable { onNavigateToSignupScreen() },
                color = SkyBlue,
                fontWeight = FontWeight.Medium,
                fontFamily = PoppinsFamily,
            )
        }
    }
}

@Composable
fun LoginContainer(
    emailValue: () -> String,
    passwordValue: () -> String,
    buttonEnabled: () -> Boolean,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginButtonClick: () -> Unit,
    onAccountTypeSelected: (AccountType) -> Unit,
    isPasswordShown: () -> Boolean,
    onTrailingIconClick: () -> Unit,
    errorHint: () -> String?,
    isLoading: () -> Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(15.dp)) {
        TextEntryModule(
            description = "Email Address",
            placeholder = "user@email.com",
            textValue = emailValue(),
            textColor = White,
            cursorColor = LightBlue,
            onValueChanged = onEmailChanged,
            trailingIcon = null,
            onTrailingIconClick = null,
            leadingIcon = Icons.Rounded.Email,
        )
        TextEntryModule(
            description = "Password",
            placeholder = "Enter Password",
            textValue = passwordValue(),
            textColor = White,
            cursorColor = LightBlue,
            onValueChanged = onPasswordChanged,
            trailingIcon = Icons.Rounded.RemoveRedEye,
            onTrailingIconClick = {
                onTrailingIconClick()
            },
            leadingIcon = Icons.Rounded.Lock,
            visualTransformation = if (isPasswordShown()) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardType = KeyboardType.Password
        )
        AccountTypeToggle(
            modifier = Modifier.fillMaxWidth(),
            onAccountTypeSelected = onAccountTypeSelected
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ){
            AuthenticationButton(
                text = "Login",
                backgroundColor = LightBlue,
                contentColor = White,
                enabled = buttonEnabled(),
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                isLoading = isLoading(),
                onButtonClick = onLoginButtonClick
            )
            Text(
                errorHint() ?: "",
                fontFamily = PoppinsFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 10.sp,
                color = Red,
                modifier = Modifier.padding(top = 5.dp).fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}