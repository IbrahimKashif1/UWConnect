package com.uwconnect.android.presentation.state

import com.uwconnect.android.domain.model.AccountType

data class LoginState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val accountType: AccountType = AccountType.Club,
    val isInputValid: Boolean = false,
    val isPasswordShown: Boolean = false,
    val errorMessageInput: String? = null,      // Related to input
    val isLoading: Boolean = false,
    val isLoginSuccess: Boolean = false,
    val errorMessageLogin: String? = null,     // Related to network/API
)
