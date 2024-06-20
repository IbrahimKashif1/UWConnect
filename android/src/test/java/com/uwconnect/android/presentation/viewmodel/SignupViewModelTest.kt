package com.uwconnect.android.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.uwconnect.android.domain.model.AccountType
import com.uwconnect.android.domain.repository.AuthRepository
import com.uwconnect.android.domain.usecase.ValidateSignupUseCase
import junit.framework.TestCase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class SignupViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var viewModel: SignupViewModel
    private val validateSignupUseCase = ValidateSignupUseCase()
    private val authRepository: AuthRepository = mock()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SignupViewModel(validateSignupUseCase, authRepository)
    }

    @Test
    fun `onNameChanged updates name and checks valid input`() = runBlockingTest {
        viewModel.onNameChanged("John Doe")
        assertEquals("John Doe", viewModel.signupState.name)
    }

    @Test
    fun `onEmailChanged updates email and checks valid input`() = runBlockingTest {
        viewModel.onEmailChanged("john@example.com")
        assertEquals("john@example.com", viewModel.signupState.email)
    }

    @Test
    fun `onPasswordChanged updates password and checks valid input`() = runBlockingTest {
        viewModel.onPasswordChanged("Password123!")
        assertEquals("Password123!", viewModel.signupState.password)
    }

    @Test
    fun `onConfirmPasswordChanged updates confirmPassword and checks valid input`() = runBlockingTest {
        viewModel.onConfirmPasswordChanged("Password123!")
        assertEquals("Password123!", viewModel.signupState.confirmPassword)
    }

    @Test
    fun `onAccountTypeChanged updates accountType`() = runBlockingTest {
        viewModel.onAccountTypeChanged(AccountType.Member)
        assertEquals(AccountType.Member, viewModel.signupState.accountType)
    }

    @Test
    fun `onPasswordVisibilityToggle toggles password visibility`() = runBlockingTest {
        val initialVisibility = viewModel.signupState.isPasswordShown
        viewModel.onPasswordVisibilityToggle()
        assertNotEquals(initialVisibility, viewModel.signupState.isPasswordShown)
    }

    @Test
    fun `onConfirmPasswordVisibilityToggle toggles confirmPassword visibility`() = runBlockingTest {
        val initialVisibility = viewModel.signupState.isPasswordRepeatedShown
        viewModel.onConfirmPasswordVisibilityToggle()
        assertNotEquals(initialVisibility, viewModel.signupState.isPasswordRepeatedShown)
    }

    @Test
    fun `onSignupClick sets isLoading to false eventually`() = runBlockingTest {
        whenever(authRepository.signup(any(), any(), any(), any())).thenReturn(true)
        viewModel.onSignupClick()
        advanceUntilIdle()
        assertFalse("isLoading should be false after coroutine completion",
            viewModel.signupState.isLoading)
        assertTrue("isSignupSuccess should be true after successful signup",
            viewModel.signupState.isSignupSuccess)
    }

    @Test
    fun `onSignupClick failure sets errorMessage`() = runBlockingTest {
        whenever(authRepository.signup(any(), any(), any(), any())).thenReturn(false)
        viewModel.onSignupClick()
        advanceUntilIdle()
        assertFalse(viewModel.signupState.isSignupSuccess)
        assertEquals("Couldn't signup! Check network or try different name/email!", viewModel.signupState.errorMessageSignup)
    }

    @Test
    fun `checkValidInput sets error message for invalid input`() = runBlockingTest {
        viewModel.onNameChanged("John Doe")
        viewModel.onEmailChanged("invalid")
        viewModel.onPasswordChanged("Password123!")
        viewModel.onConfirmPasswordChanged("Password123!")
        assertEquals("Invalid email!", viewModel.signupState.errorMessageInput)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}
