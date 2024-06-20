package com.uwconnect.android.domain.repository

import com.uwconnect.android.data.ApiClient
import com.uwconnect.android.data.ClubRepositoryImpl
import com.uwconnect.android.util.TokenManager
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.uwconnect.models.Club
import org.uwconnect.models.ClubResponse
import org.uwconnect.models.ClubsResponse
import retrofit2.Response

@ExperimentalCoroutinesApi
class ClubRepositoryImplTest {
    private val apiService = mockk<APIService>()
    private val clubRepository = ClubRepositoryImpl()
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
    fun `getProfile returns club on successful retrieval`() = runBlockingTest {
        val mockClub = Club(1,
            "Club Name",
            "Club Email",
            "Club Description",
            "FB",
            "IG",
            "DS",
            emptyList(),
            emptyList(),
            emptyList(),
        )
        coEvery { apiService.getClubProfile() } returns Response.success(ClubResponse(mockClub))
        val result = clubRepository.getProfile()
        assertNotNull(result)
        assertEquals(mockClub, result)
    }

    @Test
    fun `getProfile returns null on failure retrieval`() = runBlockingTest {
        coEvery { apiService.getClubProfile() } throws Exception()
        val result = clubRepository.getProfile()
        assertNull(result)
    }

    @Test
    fun `getAll returns list on successful retrieval`() = runBlockingTest {
        val mockClubs = listOf(Club(1,
        "Club Name",
        "Club Email",
        "Club Description",
        "FB",
        "IG",
        "DS",
        emptyList(),
        emptyList(),
        emptyList(),
        ))
        coEvery { apiService.getAllClubs() } returns Response.success(ClubsResponse(mockClubs))
        val result = clubRepository.getAll()
        assertFalse(result.isEmpty())
        assertEquals(mockClubs.size, result.size)
    }

    @Test
    fun `getAll returns empty list on failure`() = runBlockingTest {
        coEvery { apiService.getAllClubs() } throws Exception()
        val result = clubRepository.getAll()
        assertTrue(result.isEmpty())
    }

    @Test
    fun `validateClubName returns success for valid name`() = runBlockingTest {
        val validName = "Unique Club Name"
        coEvery { apiService.getAllClubs() } returns Response.success(ClubsResponse(emptyList()))
        val result = clubRepository.validateClubName(validName)
        assertTrue(result.isSuccess)
    }

    @Test
    fun `validateClubName returns failure for invalid name`() = runBlockingTest {
        val invalidName = "Existing Club Name"
        val mockClubs = listOf(Club(1,
        "Club Name",
        "Club Email",
        "Club Description",
        "FB",
        "IG",
        "DS",
        emptyList(),
        emptyList(),
        emptyList(),
        ))
        coEvery { apiService.getAllClubs() } returns Response.success(ClubsResponse(mockClubs))
        val result = clubRepository.validateClubName(invalidName)
        assertTrue(result.isFailure)
    }
}