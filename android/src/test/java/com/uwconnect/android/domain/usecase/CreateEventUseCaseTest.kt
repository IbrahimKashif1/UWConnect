package com.uwconnect.android.domain.usecase

import androidx.compose.ui.graphics.Color
import com.uwconnect.android.domain.repository.EventRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.datetime.LocalDateTime
import org.uwconnect.models.CreateEventRequest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class CreateEventUseCaseTest {
    private lateinit var repository: EventRepository
    private lateinit var createEventUseCase: CreateEventUseCase

    @Before
    fun setUp() {
        repository = mock()
        createEventUseCase = CreateEventUseCaseImpl(repository)
    }

    @Test
    fun `invoke calls repository createEvent and returns success`() = runBlockingTest {
        val eventRequest = CreateEventRequest(
            title = "Sample Event Title",
            description = "This is a sample description for our event.",
            location = "123 Main St, Anytown, AN 12345",
            link = "https://example.com/event",
            start = LocalDateTime(2024, 4, 5, 14, 30),
            end = LocalDateTime(2024, 4, 5, 17, 0),
            color = Color.Blue.toString()
        )

        val expectedResult = Result.success(Unit)
        whenever(repository.createEvent(eventRequest)).thenReturn(expectedResult)
        val result = createEventUseCase(eventRequest)

        verify(repository).createEvent(eventRequest)
        assert(result.isSuccess)
    }

    @Test
    fun `invoke calls repository createEvent and returns failure`() = runBlockingTest {
        val eventRequest = CreateEventRequest(
            title = "Sample Event Title",
            description = "This is a sample description for our event.",
            location = "123 Main St, Anytown, AN 12345",
            link = "https://example.com/event",
            start = LocalDateTime(2024, 4, 5, 14, 30),
            end = LocalDateTime(2024, 4, 5, 17, 0),
            color = Color.Green.toString()
        )

        val expectedResult = Result.failure<Unit>(RuntimeException("Error creating event"))
        whenever(repository.createEvent(eventRequest)).thenReturn(expectedResult)
        val result = createEventUseCase(eventRequest)
        verify(repository).createEvent(eventRequest)

        assert(result.isFailure)
        assert(result.exceptionOrNull()?.message == "Error creating event")
    }
}