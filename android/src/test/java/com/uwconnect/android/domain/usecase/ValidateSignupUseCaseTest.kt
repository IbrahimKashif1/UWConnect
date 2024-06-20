package com.uwconnect.android.domain.usecase

import com.uwconnect.android.domain.model.SignupValidationType
import org.junit.Assert
import org.junit.Test

class ValidateSignupUseCaseTest {
    private val validateSignupUseCase = ValidateSignupUseCase()

    @Test
    fun `when email is empty, then return EmptyField`() {
        val result = validateSignupUseCase(name = "Alex", email = "", password = "password", confirmPassword = "password")
        Assert.assertEquals(SignupValidationType.EmptyField, result)
    }

    @Test
    fun `when password is empty, then return EmptyField`() {
        val result = validateSignupUseCase(name = "Eric", email = "email@email.com", password = "", confirmPassword = "password")
        Assert.assertEquals(SignupValidationType.EmptyField, result)
    }

    @Test
    fun `when confirmPassword is empty, then return EmptyField`() {
        val result = validateSignupUseCase(name = "Sagar", email = "email@email.com", password = "password", confirmPassword = "")
        Assert.assertEquals(SignupValidationType.EmptyField, result)
    }

    @Test
    fun `when email is missing @, then return InvalidEmail`() {
        val result = validateSignupUseCase(name = "Sagar", email = "useremail.com", password = "password", confirmPassword = "password")
        Assert.assertEquals(SignupValidationType.InvalidEmail, result)
    }

    @Test
    fun `when email is missing domain, then return InvalidEmail`() {
        val result = validateSignupUseCase(name = "Sagar", email = "user@.com", password = "password", confirmPassword = "password")
        Assert.assertEquals(SignupValidationType.InvalidEmail, result)
    }

    @Test
    fun `when email is invalid, then return InvalidEmail`() {
        val result = validateSignupUseCase(name = "Sagar", email = "user@email.c", password = "password", confirmPassword = "password")
        Assert.assertEquals(SignupValidationType.InvalidEmail, result)
    }

    @Test
    fun `when email is invalid with special characters, then return InvalidEmail`() {
        val result = validateSignupUseCase(name = "Sagar", email = "us#\$er@email.c", password = "pwd", confirmPassword = "pwd")
        Assert.assertEquals(SignupValidationType.InvalidEmail, result)
    }

    @Test
    fun `when password and confirmPassword don't match, then return PasswordDontMatch`() {
        val result = validateSignupUseCase(name = "Sagar", email = "u@email.com", password = "password1", confirmPassword = "password2")
        Assert.assertEquals(SignupValidationType.PasswordDontMatch, result)
    }

    @Test
    fun `when password is too short, then return PasswordTooShort`() {
        val result = validateSignupUseCase(name = "Sagar", email = "user@email.com", password = "pass", confirmPassword = "pass")
        Assert.assertEquals(SignupValidationType.PasswordTooShort, result)
    }

    @Test
    fun `when password is missing uppercase, then return MissingUpperCasePassword`() {
        val result = validateSignupUseCase(name = "Sagar", email = "u@email.com", password = "password", confirmPassword = "password")
        Assert.assertEquals(SignupValidationType.MissingUpperCasePassword, result)
    }

    @Test
    fun `when password is missing number, then return MissingNumberPassword`() {
        val result = validateSignupUseCase(name = "Sagar", email = "u@email.com", password = "Password", confirmPassword = "Password")
        Assert.assertEquals(SignupValidationType.MissingNumberPassword, result)
    }

    @Test
    fun `when password is missing special character, then return MissingSpecialCharacterPassword`() {
        val result = validateSignupUseCase(name = "Sagar", email = "u@email.com", password = "Password1", confirmPassword = "Password1")
        Assert.assertEquals(SignupValidationType.MissingSpecialCharacterPassword, result)
    }

    @Test
    fun `when email, password and confirmPassword are valid, then return Valid`() {
        val result = validateSignupUseCase(name = "Sagar", email = "u@ema.ca", password = "Password1!", confirmPassword = "Password1!")
        Assert.assertEquals(SignupValidationType.Valid, result)
    }

    @Test
    fun `username is not provided, then return invalid`() {
        val result = validateSignupUseCase(name = "", email = "u@ema.ca", password = "Password1!", confirmPassword = "Password1!")
        Assert.assertEquals(SignupValidationType.EmptyField, result)
    }
}