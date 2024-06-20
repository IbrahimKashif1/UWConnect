package com.uwconnect.android.domain.repository

import com.uwconnect.android.data.ApiClient
import com.uwconnect.android.data.AuthRepositoryImpl
import com.uwconnect.android.domain.model.AccountType
import com.uwconnect.android.util.TokenManager
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.uwconnect.models.LoginResponse
import retrofit2.Response
import io.mockk.*
import junit.framework.TestCase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.*
import org.uwconnect.models.*

@ExperimentalCoroutinesApi
class AuthRepositoryImplTest {
    private val apiService = mockk<APIService>()
    private val authRepository = AuthRepositoryImpl()
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkObject(ApiClient)
        mockkObject(TokenManager)
        every { ApiClient.apiService } returns apiService
        coEvery { TokenManager.saveJwt(any()) } just Runs
    }

    @After
    fun tearDown() {
        unmockkObject(ApiClient)
        unmockkObject(TokenManager)
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `login returns JWT for successful member login`() = runBlockingTest {
        val jwt = "jwtToken"
        coEvery { apiService.loginAuthMember(any()) } returns Response.success(LoginResponse(jwt))
        val result = authRepository.login("email@example.com", "password", AccountType.Member)
        assertEquals(jwt, result)
        coVerify { TokenManager.saveJwt(jwt) }
    }

    @Test
    fun `login returns JWT for successful club login`() = runBlockingTest {
        val jwt = "jwtToken"
        coEvery { apiService.loginAuthClub(any()) } returns Response.success(LoginResponse(jwt))
        val result = authRepository.login("email@example.com", "password", AccountType.Club)
        assertEquals(jwt, result)
        coVerify { TokenManager.saveJwt(jwt) }
    }

    @Test
    fun `login returns empty string on failure`() = runBlockingTest {
        coEvery { apiService.loginAuthMember(any()) } returns Response.error(401, "Unauthorized".toResponseBody())
        val result = authRepository.login("wrong@example.com", "wrongpassword", AccountType.Member)
        assertEquals("", result)
    }

    @Test
    fun `signup returns true for successful member signup`() = runBlockingTest {
        coEvery { apiService.signupAuthMember(any()) } returns Response.success(SignupResponse(true.toString()))
        val result = authRepository.signup("Name", "email@example.com", "password", AccountType.Member)
        assertTrue(result)
    }

    @Test
    fun `signup returns true for successful club signup`() = runBlockingTest {
        coEvery { apiService.signupAuthClub(any()) } returns Response.success(SignupResponse(true.toString()))
        val result = authRepository.signup("Club Name", "club@example.com", "password", AccountType.Club)
        assertTrue(result)
    }


    @Test
    fun `signup returns false on failure`() = runBlockingTest {
        coEvery { apiService.signupAuthMember(any()) } returns Response.error(400, "Bad Request".toResponseBody())
        val result = authRepository.signup("Name", "email@fail.com", "password", AccountType.Member)
        assertFalse(result)
    }

    @Test
    fun `logout clears JWT and returns true`() = runBlockingTest {
        val result = authRepository.logout()
        coVerify { TokenManager.saveJwt("") }
        assertTrue(result)
    }
}
