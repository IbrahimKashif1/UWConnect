package com.uwconnect.android.domain.usecase

import com.uwconnect.android.domain.model.SignupValidationType

class ValidateSignupUseCase() {
    operator fun invoke(name: String, email: String, password: String, confirmPassword: String): SignupValidationType {
        return when {
            name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> SignupValidationType.EmptyField
            !email.contains("@") -> SignupValidationType.InvalidEmail
            !email.contains(Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")) -> SignupValidationType.InvalidEmail
            password != confirmPassword -> SignupValidationType.PasswordDontMatch
            password.length < 8 -> SignupValidationType.PasswordTooShort
            !password.contains(Regex("[A-Z]")) -> SignupValidationType.MissingUpperCasePassword
            !password.contains(Regex("[0-9]")) -> SignupValidationType.MissingNumberPassword
            !password.contains(Regex("[!@#\$%^&*(),.?\":{}|<>]")) -> SignupValidationType.MissingSpecialCharacterPassword
            else -> SignupValidationType.Valid
        }
    }
}