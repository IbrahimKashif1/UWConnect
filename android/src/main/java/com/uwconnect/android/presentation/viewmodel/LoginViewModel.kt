package com.uwconnect.android.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uwconnect.android.domain.model.AccountType
import com.uwconnect.android.domain.model.LoginValidationType
import com.uwconnect.android.domain.repository.AuthRepository
import com.uwconnect.android.domain.usecase.ValidateLoginUseCase
import com.uwconnect.android.presentation.state.LoginState
import com.uwconnect.android.util.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.uwconnect.models.LoginResponse
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val validateLoginUseCase: ValidateLoginUseCase,
    private val authRepository: AuthRepository
): ViewModel() {
    var loginState by mutableStateOf(LoginState())

    private val _userType = MutableStateFlow<String?>(null)
    val userType: StateFlow<String?> = _userType

    fun onEmailChanged(email: String) {
        loginState = loginState.copy(email = email)
        checkValidInput()
    }

    fun onPasswordChanged(password: String) {
        loginState = loginState.copy(password = password)
        checkValidInput()
    }

    fun onAccountTypeChanged(accountType: AccountType) {
        loginState = loginState.copy(accountType = accountType)
    }

    fun onPasswordVisibilityToggle() {
        loginState = loginState.copy(isPasswordShown = !loginState.isPasswordShown)
    }

    fun onLoginClick() {
        loginState = loginState.copy(isLoading = true)
        viewModelScope.launch {
            val jwt = authRepository.login(loginState.email, loginState.password, loginState.accountType)

            if (jwt != "") {
                _userType.value = TokenManager.parseJwt(jwt).type
                loginState = loginState.copy(isLoginSuccess = true)
                loginState = loginState.copy(errorMessageLogin = null)
            } else {
                loginState = loginState.copy(isLoginSuccess = false)
                loginState = loginState.copy(errorMessageLogin = "Couldn't login! Check network or credentials.")
            }
            loginState = loginState.copy(isLoading = false)
        }
    }

    private fun checkValidInput() {
        val validationResponse = validateLoginUseCase(loginState.email, loginState.password)
        processInputValidationType(validationResponse)
    }

    private fun processInputValidationType(type: LoginValidationType) {
        loginState = when (type) {
            LoginValidationType.EmptyField -> loginState.copy(
                isInputValid = false,
                errorMessageInput = "Field cannot be empty!"
            )
            LoginValidationType.InvalidEmail -> loginState.copy(
                isInputValid = false,
                errorMessageInput = "Invalid email!"
            )
            LoginValidationType.Valid -> loginState.copy(
                isInputValid = true,
                errorMessageInput = null
            )
        }
    }
}
