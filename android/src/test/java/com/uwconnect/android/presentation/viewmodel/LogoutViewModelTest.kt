package com.uwconnect.android.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.uwconnect.android.domain.repository.AuthRepository
import junit.framework.TestCase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class LogoutViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var viewModel: LogoutViewModel
    private val authRepository: AuthRepository = mock()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LogoutViewModel(authRepository)
    }

    @Test
    fun `onLogoutClick sets isLoading to false once complete`() = runBlockingTest {
        whenever(authRepository.logout()).thenReturn(true)
        viewModel.onLogoutClick()
        advanceUntilIdle()
        assertFalse("isLoading should be false after coroutine completion", viewModel.logoutState.isLoading)
    }

    @Test
    fun `onLogoutClick success sets isLogoutSuccess to true`() = runBlockingTest {
        whenever(authRepository.logout()).thenReturn(true)
        viewModel.onLogoutClick()
        advanceUntilIdle()
        assertTrue("isLogoutSuccess should be true after successful logout", viewModel.logoutState.isLogoutSuccess)
    }

    @Test
    fun `onLogoutClick failure sets errorMessageLogout`() = runBlockingTest {
        whenever(authRepository.logout()).thenReturn(false)
        viewModel.onLogoutClick()
        advanceUntilIdle()
        assertFalse("isLogoutSuccess should be false on logout failure", viewModel.logoutState.isLogoutSuccess)
        assertEquals("An error occurred", viewModel.logoutState.errorMessageLogout)
    }

    @Test
    fun `onClearErrorMessage clears errorMessageLogout`() = runBlockingTest {
        viewModel.logoutState = viewModel.logoutState.copy(errorMessageLogout = "An error occurred")
        viewModel.onClearErrorMessage()
        assertNull("errorMessageLogout should be null after onClearErrorMessage", viewModel.logoutState.errorMessageLogout)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}