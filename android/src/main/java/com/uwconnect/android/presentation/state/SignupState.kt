package com.uwconnect.android.presentation.state

import com.uwconnect.android.domain.model.AccountType

data class SignupState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val accountType: AccountType = AccountType.Club,
    val isInputValid: Boolean = false,
    val isPasswordShown: Boolean = false,
    val isPasswordRepeatedShown: Boolean = false,
    val errorMessageInput: String? = null,        // Related to input
    val isLoading: Boolean = false,
    val isSignupSuccess: Boolean = false,
    val errorMessageSignup: String? = null        // Related to network/API
)
