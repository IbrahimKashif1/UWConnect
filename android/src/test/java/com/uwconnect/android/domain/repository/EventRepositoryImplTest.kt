package com.uwconnect.android.domain.repository

import com.uwconnect.android.data.ApiClient
import com.uwconnect.android.data.AuthRepositoryImpl
import com.uwconnect.android.data.EventRepositoryImpl
import com.uwconnect.android.domain.model.AccountType
import com.uwconnect.android.util.TokenManager
import io.mockk.*
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDateTime
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.uwconnect.models.*

@ExperimentalCoroutinesApi
class EventRepositoryImplTest {
    private val apiService = mockk<APIService>()
    private val eventRepository = EventRepositoryImpl()
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
    fun `getEventById returns event on successful retrieval`() = runBlockingTest {
        val mockEvent = Event(
            1,
            "Event Name",
            "Description",
            "location",
            "link",
            "color",
            Club(1, "Club Name", "Club Email", "Club Description",
                "FB", "IG", "DS", emptyList(), emptyList(), emptyList()),
            start = LocalDateTime(2021, 1, 1, 1, 1),
            end = LocalDateTime(2021, 1, 1, 1, 1),
            emptyList()
        )
        coEvery { apiService.getEventById(any()) } returns Response.success(EventResponse(mockEvent))
        val result = eventRepository.getEventById(1)
        assertNotNull(result)
        assertEquals(mockEvent, result)
    }

    @Test
    fun `getEventById returns null on failure retrieval`() = runBlockingTest {
        coEvery { apiService.getEventById(any()) } throws Exception()
        val result = eventRepository.getEventById(1)
        assertNull(result)
    }

    @Test
    fun `createEvent returns success on successful creation`() = runBlockingTest {
        val createEventRequest = CreateEventRequest(
            "Event Name",
            "Description",
            "Location",
            "Link",
            "Color",
            LocalDateTime(2021, 1, 1, 1, 1),
            LocalDateTime(2021, 1, 1, 1, 1)
        )
        coEvery { apiService.createEvent(createEventRequest) } returns Response.success(Unit)
        val result = eventRepository.createEvent(createEventRequest)
        assertTrue(result.isSuccess)
    }

    @Test
    fun `createEvent returns failure when an error occurs`() = runBlockingTest {
        val createEventRequest = CreateEventRequest(
            "Event Name",
            "Description",
            "Location",
            "Link",
            "Color",
            start = LocalDateTime(2021, 1, 1, 1, 1),
            end = LocalDateTime(2021, 1, 1, 1, 1),
        )
        coEvery { apiService.createEvent(createEventRequest) } returns Response.error(400, "".toResponseBody())
        val result = eventRepository.createEvent(createEventRequest)
        assertTrue(result.isFailure)
    }

    @Test
    fun `updateEvent returns success on successful update`() = runBlockingTest {
        val updateEventRequest = UpdateEventRequest(
            "title", "description", LocalDateTime(2021, 1, 1, 1, 1),
            LocalDateTime(2021, 1, 1, 1, 1), "loc", "link", "color"
        )
        coEvery { apiService.updateEvent(any(), updateEventRequest) } returns Response.success(Unit)
        val result = eventRepository.updateEvent(1, updateEventRequest)
        assertTrue(result.isSuccess)
    }

    @Test
    fun `updateEvent returns failure when an error occurs`() = runBlockingTest {
        val updateEventRequest = UpdateEventRequest(
            "title", "description", LocalDateTime(2021, 1, 1, 1, 1),
            LocalDateTime(2021, 1, 1, 1, 1), "loc", "link", "color"
        )
        coEvery { apiService.updateEvent(any(), updateEventRequest) } throws Exception()
        val result = eventRepository.updateEvent(1, updateEventRequest)
        assertTrue(result.isFailure)
    }

    @Test
    fun `deleteEvent returns success on successful deletion`() = runBlockingTest {
        coEvery { apiService.deleteEvent(any()) } returns Response.success(Unit)
        val result = eventRepository.deleteEvent(1)
        assertTrue(result.isSuccess)
    }

    @Test
    fun `deleteEvent returns failure when an error occurs`() = runBlockingTest {
        coEvery { apiService.deleteEvent(any()) } throws Exception()
        val result = eventRepository.deleteEvent(1)
        assertTrue(result.isFailure)
    }

    @Test
    fun `joinEvent returns success on successful join`() = runBlockingTest {
        coEvery { apiService.joinEvent(any()) } returns Response.success(Unit)
        val result = eventRepository.joinEvent(1)
        assertTrue(result.isSuccess)
    }

    @Test
    fun `joinEvent returns failure when an error occurs`() = runBlockingTest {
        coEvery { apiService.joinEvent(any()) } throws Exception()
        val result = eventRepository.joinEvent(1)
        assertTrue(result.isFailure)
    }

    @Test
    fun `leaveEvent returns success on successful leave`() = runBlockingTest {
        coEvery { apiService.leaveEvent(any()) } returns Response.success(Unit)
        val result = eventRepository.leaveEvent(1)
        assertTrue(result.isSuccess)
    }

    @Test
    fun `leaveEvent returns failure when an error occurs`() = runBlockingTest {
        coEvery { apiService.leaveEvent(any()) } throws Exception()
        val result = eventRepository.leaveEvent(1)
        assertTrue(result.isFailure)
    }
}
