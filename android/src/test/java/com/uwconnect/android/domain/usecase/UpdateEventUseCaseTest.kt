package com.uwconnect.android.domain.usecase

import com.uwconnect.android.domain.repository.EventRepository
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDateTime
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.*
import org.uwconnect.models.UpdateEventRequest

class UpdateEventUseCaseTest {
    private val mockRepository = mock(EventRepository::class.java)
    private val updateEventUseCase = UpdateEventUseCaseImpl(mockRepository)

    @Test
    fun `invoke returns success when updateEvent is successful`() = runBlocking {
        val eventId = 1
        val updateEventRequest = UpdateEventRequest(
            "New Event Title",
            "New Description",
            LocalDateTime(2023, 12, 31, 0, 0),
            LocalDateTime(2023, 12, 31, 23, 59),
            "New Location",
            "New Link",
            "New Color"
        )
        `when`(mockRepository.updateEvent(eventId, updateEventRequest)).thenReturn(Result.success(Unit))

        val result = updateEventUseCase.invoke(eventId, updateEventRequest)

        verify(mockRepository).updateEvent(eventId, updateEventRequest)
        assertTrue(result.isSuccess)
    }

    @Test
    fun `invoke returns failure when updateEvent is unsuccessful`() = runBlocking {
        val eventId = 1
        val updateEventRequest = UpdateEventRequest(
            "New Event Title",
            "New Description",
            LocalDateTime(2023, 12, 31, 0, 0),
            LocalDateTime(2023, 12, 31, 23, 59),
            "New Location",
            "New Link",
            "New Color"
        )
        val exception = RuntimeException("Error updating event")
        `when`(mockRepository.updateEvent(eventId, updateEventRequest)).thenReturn(Result.failure(exception))

        val result = updateEventUseCase.invoke(eventId, updateEventRequest)

        verify(mockRepository).updateEvent(eventId, updateEventRequest)
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
