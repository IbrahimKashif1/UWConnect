package com.uwconnect.android.domain.usecase

import androidx.compose.ui.graphics.Color
import com.uwconnect.android.domain.repository.EventRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.runTest
import org.uwconnect.models.Event
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlinx.coroutines.test.runBlockingTest
import org.mockito.Mockito
import org.mockito.Mockito.lenient

@ExperimentalCoroutinesApi
class GetEventByIdUseCaseTest {
    private var eventRepository: EventRepository = mock()
    private var getEventByIdUseCase: GetEventByIdUseCase = GetEventByIdUseCaseImpl(eventRepository)

    @Before
    fun setUp() {}

    @Test
    fun `invoke with valid eventId returns Event`() = runBlocking {
        val mockEvent = Event(
            id = 1,
            title = "Event",
            description = "Description",
            location = "Location",
            link = "Link",
            club = mock(),
            start = mock(),
            end = mock(),
            participants = listOf(),
            color = Color.Red.toString()
        )

        lenient().`when`(eventRepository.getEventById(1)).thenReturn(mockEvent)
        val result = getEventByIdUseCase(1)
        assertEquals(mockEvent, result)
    }

    @Test
    fun `invoke with invalid eventId returns null`() = runBlocking {
        lenient().`when`(eventRepository.getEventById(2)).thenReturn(null)
        val result = getEventByIdUseCase(2)
        assertNull(result)
    }

    @After
    fun tearDown() {}
}
