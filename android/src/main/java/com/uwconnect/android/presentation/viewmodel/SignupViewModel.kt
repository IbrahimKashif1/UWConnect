package com.uwconnect.android.presentation.viewmodel

import com.uwconnect.android.domain.model.SignupValidationType
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uwconnect.android.domain.model.AccountType
import com.uwconnect.android.domain.repository.AuthRepository
import com.uwconnect.android.presentation.state.SignupState
import com.uwconnect.android.domain.usecase.ValidateSignupUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val validateSignupUseCase: ValidateSignupUseCase,
    private val authRepository: AuthRepository
): ViewModel() {

    var signupState by mutableStateOf(SignupState())
        private set

    fun onNameChanged(name: String) {
        signupState = signupState.copy(name = name)
        checkValidInput()
    }

    fun onEmailChanged(email: String) {
        signupState = signupState.copy(email = email)
        checkValidInput()
    }

    fun onPasswordChanged(password: String) {
        signupState = signupState.copy(password = password)
        checkValidInput()
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        signupState = signupState.copy(confirmPassword = confirmPassword)
        checkValidInput()
    }

    fun onAccountTypeChanged(accountType: AccountType) {
        signupState = signupState.copy(accountType = accountType)
    }

    fun onPasswordVisibilityToggle() {
        signupState = signupState.copy(isPasswordShown = !signupState.isPasswordShown)
    }

    fun onConfirmPasswordVisibilityToggle() {
        signupState = signupState.copy(isPasswordRepeatedShown = !signupState.isPasswordRepeatedShown)
    }

    fun onSignupClick() {
        signupState = signupState.copy(isLoading = true)
        viewModelScope.launch {
            val signupResult = authRepository.signup(
                    name = signupState.name,
                    email = signupState.email,
                    password = signupState.password,
                    accountType = signupState.accountType)
            signupState = signupState.copy(isSignupSuccess = signupResult)
            if (!signupResult) {
                signupState = signupState.copy(errorMessageSignup = "Couldn't signup! Check network or try different name/email!")
            }
            signupState = signupState.copy(isLoading = false)
        }
    }

    fun checkValidInput() {
        val validationResponse = validateSignupUseCase(signupState.name, signupState.email, signupState.password, signupState.confirmPassword)
        processInputValidationType(validationResponse)
    }

    private fun processInputValidationType(type: SignupValidationType) {
        signupState = when (type) {
            SignupValidationType.EmptyField -> signupState.copy(
                isInputValid = false,
                errorMessageInput = "Field cannot be empty!"
            )
            SignupValidationType.InvalidEmail -> signupState.copy(
                isInputValid = false,
                errorMessageInput = "Invalid email!"
            )
            SignupValidationType.PasswordTooShort -> signupState.copy(
                isInputValid = false,
                errorMessageInput = "Password must be at least 8 characters long!"
            )
            SignupValidationType.MissingUpperCasePassword -> signupState.copy(
                isInputValid = false,
                errorMessageInput = "Password must contain at least one uppercase letter!"
            )
            SignupValidationType.MissingNumberPassword -> signupState.copy(
                isInputValid = false,
                errorMessageInput = "Password must contain at least one number!"
            )
            SignupValidationType.MissingSpecialCharacterPassword -> signupState.copy(
                isInputValid = false,
                errorMessageInput = "Password must contain at least one special character!"
            )
            SignupValidationType.PasswordDontMatch -> signupState.copy(
                isInputValid = false,
                errorMessageInput = "Passwords do not match!"
            )
            SignupValidationType.Valid -> signupState.copy(
                isInputValid = true,
                errorMessageInput = null
            )
        }
    }
}