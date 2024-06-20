package com.uwconnect.android.domain.usecase

import com.uwconnect.android.domain.repository.EventRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.*

class LeaveEventUseCaseTest {
    private val mockRepository = mock(EventRepository::class.java)
    private val leaveEventUseCase = LeaveEventUseCaseImpl(mockRepository)

    @Test
    fun `invoke returns success when leaveEvent is successful`() = runBlocking {
        val eventId = 1
        `when`(mockRepository.leaveEvent(eventId)).thenReturn(Result.success(Unit))

        val result = leaveEventUseCase.invoke(eventId)

        verify(mockRepository).leaveEvent(eventId)
        assertTrue(result.isSuccess)
    }

    @Test
    fun `invoke returns failure when leaveEvent is unsuccessful`() = runBlocking {
        val eventId = 1
        val exception = RuntimeException("Error leaving event")
        `when`(mockRepository.leaveEvent(eventId)).thenReturn(Result.failure(exception))

        val result = leaveEventUseCase.invoke(eventId)

        verify(mockRepository).leaveEvent(eventId)
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
