package com.uwconnect.android.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.RemoveRedEye
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.uwconnect.android.domain.model.AccountType
import com.uwconnect.android.presentation.components.AccountTypeToggle
import com.uwconnect.android.presentation.components.AuthenticationButton
import com.uwconnect.android.presentation.components.NavigationHelper
import com.uwconnect.android.presentation.components.TextEntryModule
import com.uwconnect.android.presentation.viewmodel.SignupViewModel
import com.uwconnect.android.presentation.ui.*

@Composable
fun SignupScreen(
    onNavigateToLoginScreen: () -> Unit,
    signupViewModel: SignupViewModel = hiltViewModel()
) {
    NavigationHelper(
        shouldNavigate = {
            signupViewModel.signupState.isSignupSuccess
        },
        destination = {
            onNavigateToLoginScreen()
        }
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .padding(top = 16.dp, bottom = 8.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = "Sign Up",
                    color = White,
                    fontSize = 50.sp,
                    fontFamily = PoppinsFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontStyle = FontStyle.Normal
                )
            }
        }
        item {
            SignUpContainer(
                usernameValue = {
                    signupViewModel.signupState.name
                },
                emailValue = {
                    signupViewModel.signupState.email
                },
                passwordValue = {
                    signupViewModel.signupState.password
                },
                passwordRepeatedValue = {
                    signupViewModel.signupState.confirmPassword
                },
                buttonEnabled = {
                    signupViewModel.signupState.isInputValid
                },
                onNameChanged = signupViewModel::onNameChanged,
                onEmailChanged = signupViewModel::onEmailChanged,
                onPasswordChanged = signupViewModel::onPasswordChanged,
                onPasswordRepeatedChanged = signupViewModel::onConfirmPasswordChanged,
                onAccountTypeSelected = signupViewModel::onAccountTypeChanged,
                onButtonClick = signupViewModel::onSignupClick,
                isPasswordShown = {
                    signupViewModel.signupState.isPasswordShown
                },
                isPasswordRepeatedShown = {
                    signupViewModel.signupState.isPasswordRepeatedShown
                },
                onTrailingPasswordIconClick = {
                    signupViewModel.onPasswordVisibilityToggle()
                },
                onTrailingPasswordRepeatedIconClick = {
                    signupViewModel.onConfirmPasswordVisibilityToggle()
                },
                errorHint = {
                    if (signupViewModel.signupState.errorMessageInput != null) {
                        signupViewModel.signupState.errorMessageInput
                    } else if (signupViewModel.signupState.errorMessageSignup != null) {
                        signupViewModel.signupState.errorMessageSignup
                    } else {
                        null
                    }
                },
                isLoading = {
                    signupViewModel.signupState.isLoading
                },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(DarkBlue)
                    .padding(10.dp, 15.dp, 10.dp, 5.dp)
            )
        }
        item {
            Row(
                modifier = Modifier
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "Already have an account?",
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp,
                    fontFamily = PoppinsFamily,
                    color = White
                )
                Text(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .clickable {
                            onNavigateToLoginScreen()
                        },
                    text = "Login!",
                    color = SkyBlue,
                    fontWeight = FontWeight.Medium,
                    fontFamily = PoppinsFamily,
                )
            }
        }
    }
}

@Composable
fun SignUpContainer(
    usernameValue: () -> String,
    emailValue:() -> String,
    passwordValue: () -> String,
    passwordRepeatedValue: () -> String,
    buttonEnabled: () -> Boolean,
    onNameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onPasswordRepeatedChanged: (String) -> Unit,
    onAccountTypeSelected: (AccountType) -> Unit,
    onButtonClick: () -> Unit,
    isPasswordShown: () -> Boolean,
    isPasswordRepeatedShown: () -> Boolean,
    onTrailingPasswordIconClick: () -> Unit,
    onTrailingPasswordRepeatedIconClick: () -> Unit,
    errorHint: () -> String?,
    isLoading: () -> Boolean,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TextEntryModule(
            modifier = Modifier
                .fillMaxWidth(),
            description = "Name / Club Name",
            placeholder = "John Doe",
            leadingIcon = Icons.Rounded.Person,
            textValue = usernameValue(),
            textColor = White,
            cursorColor = LightBlue,
            onValueChanged = onNameChanged,
            trailingIcon = null,
            onTrailingIconClick = null
        )
        TextEntryModule(
            modifier = Modifier
                .fillMaxWidth(),
            description = "Email Address",
            placeholder = "user@email.com",
            leadingIcon = Icons.Rounded.Email,
            textValue = emailValue(),
            textColor = White,
            cursorColor = LightBlue,
            onValueChanged = onEmailChanged,
            trailingIcon = null,
            onTrailingIconClick = null
        )
        TextEntryModule(
            modifier = Modifier.fillMaxWidth(),
            description = "Password",
            placeholder = "Enter Password",
            leadingIcon = Icons.Rounded.Lock,
            textValue = passwordValue(),
            textColor = White,
            cursorColor = LightBlue,
            onValueChanged = onPasswordChanged,
            keyboardType = KeyboardType.Password,
            visualTransformation = if(isPasswordShown()){
                VisualTransformation.None
            } else PasswordVisualTransformation(),
            trailingIcon = Icons.Rounded.RemoveRedEye,
            onTrailingIconClick = {
                onTrailingPasswordIconClick()
            }
        )
        TextEntryModule(
            modifier = Modifier.fillMaxWidth(),
            description = "Confirm Password",
            placeholder = "Re-enter Password",
            leadingIcon = Icons.Rounded.Lock,
            textValue = passwordRepeatedValue(),
            textColor = White,
            cursorColor = LightBlue,
            onValueChanged = onPasswordRepeatedChanged,
            keyboardType = KeyboardType.Password,
            visualTransformation = if(isPasswordRepeatedShown()){
                VisualTransformation.None
            } else PasswordVisualTransformation(),
            trailingIcon = Icons.Rounded.RemoveRedEye,
            onTrailingIconClick = {
                onTrailingPasswordRepeatedIconClick()
            }
        )
        AccountTypeToggle(
            modifier = Modifier.fillMaxWidth(),
            onAccountTypeSelected = onAccountTypeSelected
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ){
            AuthenticationButton(
                text = "Sign Up",
                backgroundColor = LightBlue,
                contentColor = White,
                enabled = buttonEnabled(),
                modifier = Modifier.padding(top = 10.dp).fillMaxWidth().height(50.dp),
                onButtonClick = onButtonClick,
                isLoading = isLoading()
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
