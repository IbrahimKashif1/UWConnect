package com.uwconnect.android.domain.usecase

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.*
import com.uwconnect.android.domain.repository.EventRepository

class JoinEventUseCaseTest {
    private val mockRepository = mock(EventRepository::class.java)
    private val joinEventUseCase = JoinEventUseCaseImpl(mockRepository)

    @Test
    fun `invoke returns success when joinEvent is successful`() = runBlocking {
        val eventId = 1
        `when`(mockRepository.joinEvent(eventId)).thenReturn(Result.success(Unit))

        val result = joinEventUseCase.invoke(eventId)

        verify(mockRepository).joinEvent(eventId)
        assertTrue(result.isSuccess)
    }

    @Test
    fun `invoke returns failure when joinEvent is unsuccessful`() = runBlocking {
        val eventId = 1
        val exception = RuntimeException("Error joining event")
        `when`(mockRepository.joinEvent(eventId)).thenReturn(Result.failure(exception))

        val result = joinEventUseCase.invoke(eventId)

        verify(mockRepository).joinEvent(eventId)
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
