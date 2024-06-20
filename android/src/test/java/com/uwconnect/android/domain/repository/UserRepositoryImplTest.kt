package com.uwconnect.android.domain.repository

import com.uwconnect.android.data.ApiClient
import com.uwconnect.android.data.UserRepositoryImpl
import io.mockk.*
import junit.framework.TestCase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDateTime
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.uwconnect.models.*
import retrofit2.Response

@ExperimentalCoroutinesApi
class UserRepositoryImplTest {
    private val apiService = mockk<APIService>()
    private val userRepository = UserRepositoryImpl()
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkObject(ApiClient)
        every { ApiClient.apiService } returns apiService
    }

    @After
    fun tearDown() {
        unmockkObject(ApiClient) // Unmock the ApiClient object
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `getProfile returns user on successful retrieval`() = runBlockingTest {
        val mockUser = User(
            1,
            "test",
            "test",
            "test",
            emptyList(),
            emptyList(),
        )
        coEvery { apiService.getUserProfile() } returns Response.success(UserResponse(mockUser))
        val result = userRepository.getProfile()
        assertNotNull(result)
        assertEquals(mockUser, result)
    }

    @Test
    fun `getProfile returns null on failure retrieval`() = runBlockingTest {
        coEvery { apiService.getUserProfile() } throws Exception()
        val result = userRepository.getProfile()
        assertNull(result)
    }


    @Test
    fun `updateProfile returns Result success on successful update`() = runBlockingTest {
        val mockUserRequest = UpdateUserRequest("new name", "new bio")
        coEvery { apiService.updateUserProfile(any()) } returns Response.success(Unit)
        val result = userRepository.updateProfile(mockUserRequest)
        assertTrue(result.isSuccess)
    }

    @Test
    fun `updateProfile returns Result failure when an exception occurs`() = runBlockingTest {
        val mockUserRequest = UpdateUserRequest("new name", "new bio")
        coEvery { apiService.updateUserProfile(any()) } throws Exception()
        val result = userRepository.updateProfile(mockUserRequest)
        assertTrue(result.isFailure)
    }

    @Test
    fun `getAnnouncements returns list on successful retrieval`() = runBlockingTest {
        val mockAnnouncements = listOf(Announcement(
            1, "title", "description", Club(
                1, "n", "e", "d", "ig", "fb", "ds", emptyList(), emptyList(), emptyList()),
            timestamp = LocalDateTime(2021, 1, 1, 1, 1)
            )
        )
        coEvery { apiService.getUserAnnouncements() } returns Response.success(AnnouncementsResponse(mockAnnouncements))
        val result = userRepository.getAnnouncements()
        assertFalse(result.isEmpty())
        assertEquals(mockAnnouncements.size, result.size)
    }
}