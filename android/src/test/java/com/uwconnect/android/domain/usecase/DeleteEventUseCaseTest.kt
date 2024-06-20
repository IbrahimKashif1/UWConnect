package com.uwconnect.android.domain.usecase

import com.uwconnect.android.domain.repository.EventRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlinx.coroutines.test.runBlockingTest

@ExperimentalCoroutinesApi
class DeleteEventUseCaseTest {
    private lateinit var repository: EventRepository
    private lateinit var deleteEventUseCase: DeleteEventUseCase

    @Before
    fun setUp() {
        repository = mock()
        deleteEventUseCase = DeleteEventUseCaseImpl(repository)
    }

    @Test
    fun `deleteEvent calls repository deleteEvent with correct eventId and returns success`() = runBlockingTest {
        val eventId = 1
        val expectedResult = Result.success(Unit)
        whenever(repository.deleteEvent(eventId)).thenReturn(expectedResult)

        val result = deleteEventUseCase(eventId)

        verify(repository).deleteEvent(eventId)
        assert(result.isSuccess)
    }

    @Test
    fun `deleteEvent calls repository deleteEvent with correct eventId and returns failure`() = runBlockingTest {
        val eventId = 1
        val expectedResult = Result.failure<Unit>(Exception("Deletion failed"))
        whenever(repository.deleteEvent(eventId)).thenReturn(expectedResult)

        val result = deleteEventUseCase(eventId)

        verify(repository).deleteEvent(eventId)
        assert(result.isFailure)
        assertEquals("Deletion failed", result.exceptionOrNull()?.message)
    }
}