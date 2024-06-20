package com.uwconnect.android.domain.model

enum class SignupValidationType {
    EmptyField,
    InvalidEmail,
    PasswordDontMatch,
    MissingUpperCasePassword,
    MissingNumberPassword,
    MissingSpecialCharacterPassword,
    PasswordTooShort,
    Valid
}