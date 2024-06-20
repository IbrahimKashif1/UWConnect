package com.uwconnect.android.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.uwconnect.android.domain.model.AccountType
import com.uwconnect.android.domain.model.LoginValidationType
import com.uwconnect.android.domain.repository.AuthRepository
import com.uwconnect.android.domain.usecase.ValidateLoginUseCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner.Silent::class)
class LoginViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: LoginViewModel
    private val validateLoginUseCase: ValidateLoginUseCase = mock()
    private val authRepository: AuthRepository = mock()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(validateLoginUseCase, authRepository)
    }

    @Test
    fun `onEmailChanged updates email and checks valid input`() = runBlockingTest {
        whenever(validateLoginUseCase(any(), any())).thenReturn(LoginValidationType.Valid)
        viewModel.onEmailChanged("john@example.com")
        assertEquals("john@example.com", viewModel.loginState.email)
    }

    @Test
    fun `onPasswordChanged updates password and checks valid input`() = runBlockingTest {
        whenever(validateLoginUseCase(any(), any())).thenReturn(LoginValidationType.Valid)
        viewModel.onPasswordChanged("Password123!")
        assertEquals("Password123!", viewModel.loginState.password)
    }

    @Test
    fun `onAccountTypeChanged updates accountType`() = runBlockingTest {
        viewModel.onAccountTypeChanged(AccountType.Member)
        assertEquals(AccountType.Member, viewModel.loginState.accountType)
    }

    @Test
    fun `onPasswordVisibilityToggle toggles password visibility`() = runBlockingTest {
        val initialVisibility = viewModel.loginState.isPasswordShown
        viewModel.onPasswordVisibilityToggle()
        assertNotEquals(initialVisibility, viewModel.loginState.isPasswordShown)
    }

    @Test
    fun `onLoginClick unsuccessful login sets errorMessageLogin`() = runBlockingTest {
        val email = "wrong@example.com"
        val password = "wrongPassword"
        val accountType = AccountType.Member

        viewModel.loginState = viewModel.loginState.copy(email = email, password = password, accountType = accountType)
        whenever(authRepository.login(email, password, accountType)).thenReturn("")

        viewModel.onLoginClick()
        advanceUntilIdle()

        assertFalse(viewModel.loginState.isLoginSuccess)
        assertEquals(null, viewModel.loginState.errorMessageLogin)
    }

    @Test
    fun `checkValidInput sets error message for invalid input`() = runBlockingTest {
        whenever(validateLoginUseCase(any(), any())).thenReturn(LoginValidationType.InvalidEmail)
        viewModel.onEmailChanged("invalid")
        assertEquals("Invalid email!", viewModel.loginState.errorMessageInput)
    }

    @Test
    fun `checkValidInput with empty field sets appropriate error message`() = runBlockingTest {
        whenever(validateLoginUseCase("", "")).thenReturn(LoginValidationType.EmptyField)
        viewModel.onEmailChanged("")
        viewModel.onPasswordChanged("")
        assertEquals("Field cannot be empty!", viewModel.loginState.errorMessageInput)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}