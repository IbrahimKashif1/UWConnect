package com.uwconnect.android.domain.usecase

import com.uwconnect.android.domain.model.LoginValidationType
import org.junit.Assert
import org.junit.Test

class ValidateLoginUseCaseTest {
    private val validateLoginUseCase = ValidateLoginUseCase()

    @Test
    fun `when email is empty, then return EmptyField`() {
        val result = validateLoginUseCase(email = "", password = "password")
        Assert.assertEquals(LoginValidationType.EmptyField, result)
    }

    @Test
    fun `when password is empty, then return EmptyField`() {
        val result = validateLoginUseCase(email = "email@gmail.com", password = "")
        Assert.assertEquals(LoginValidationType.EmptyField, result)
    }

    @Test
    fun `when email is invalid, then return NoEmail`() {
        val result = validateLoginUseCase(email = "useremail.com", password = "password")
        Assert.assertEquals(LoginValidationType.InvalidEmail, result)
    }

    @Test
    fun `when email and password are valid, then return Valid`() {
        val result = validateLoginUseCase(email = "email@gmail.com", password = "password")
        Assert.assertEquals(LoginValidationType.Valid, result)
    }
}
