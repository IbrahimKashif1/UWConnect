package com.uwconnect.android.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import com.uwconnect.android.util.JWT
import com.uwconnect.android.util.TokenManager
import io.mockk.coEvery
import io.mockk.mockkObject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import kotlin.coroutines.ContinuationInterceptor

@ExperimentalCoroutinesApi
class AuthViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        mockkObject(TokenManager)
        viewModel = AuthViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadAuthState with valid JWT, updates states correctly`() = testCoroutineRule.runBlockingTest {
        coEvery { TokenManager.getJwt() } returns "valid.jwt.token"
        coEvery { TokenManager.parseJwt(any()) } returns JWT(123, "User")
        viewModel.loadAuthState()
        viewModel.viewModelScope.launch {
            assertEquals(false, viewModel.isLoading.first())
            assertEquals(123, viewModel.id.value)
            assertEquals("User", viewModel.type.value)
            assertEquals(true, viewModel.isAuthenticated.value)
        }
    }

    @Test
    fun `loadAuthState with invalid JWT, updates states correctly`() = testCoroutineRule.runBlockingTest {
        coEvery { TokenManager.getJwt() } returns ""
        coEvery { TokenManager.parseJwt(any()) } returns JWT(0, "")
        viewModel.loadAuthState()
        viewModel.viewModelScope.launch {
            assertEquals(false, viewModel.isLoading.first())
            assertEquals(0, viewModel.id.value)
            assertEquals("", viewModel.type.value)
            assertEquals(false, viewModel.isAuthenticated.value)
        }
    }
}

@ExperimentalCoroutinesApi
class TestCoroutineRule : TestWatcher(), TestCoroutineScope by TestCoroutineScope() {
    override fun starting(description: Description?) {
        if (description != null) {
            super.starting(description)
        }
        Dispatchers.setMain(this.coroutineContext[ContinuationInterceptor] as CoroutineDispatcher)
    }

    override fun finished(description: Description?) {
        if (description != null) {
            super.finished(description)
        }
        Dispatchers.resetMain()
    }
}