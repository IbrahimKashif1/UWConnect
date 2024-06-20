package com.uwconnect.android.domain.usecase

import com.uwconnect.android.domain.model.LoginValidationType

class ValidateLoginUseCase() {
    operator fun invoke(email: String, password: String): LoginValidationType {
        return when {
            email.isEmpty() || password.isEmpty() -> LoginValidationType.EmptyField
            !email.contains("@") -> LoginValidationType.InvalidEmail
            else -> LoginValidationType.Valid
        }
    }
}
