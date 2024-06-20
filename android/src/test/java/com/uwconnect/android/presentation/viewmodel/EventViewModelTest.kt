package com.uwconnect.android.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import com.uwconnect.android.domain.usecase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.uwconnect.models.CreateEventRequest
import org.uwconnect.models.Event
import org.uwconnect.models.UpdateEventRequest

@ExperimentalCoroutinesApi
class EventViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    @Mock
    private lateinit var getEventByIdUseCase: GetEventByIdUseCase
    @Mock
    private lateinit var createEventUseCase: CreateEventUseCase
    @Mock
    private lateinit var updateEventUseCase: UpdateEventUseCase
    @Mock
    private lateinit var deleteEventUseCase: DeleteEventUseCase
    @Mock
    private lateinit var joinEventUseCase: JoinEventUseCase
    @Mock
    private lateinit var leaveEventUseCase: LeaveEventUseCase
    private lateinit var viewModel: EventViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = EventViewModel(
            getEventByIdUseCase,
            createEventUseCase,
            updateEventUseCase,
            deleteEventUseCase,
            joinEventUseCase,
            leaveEventUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getEventById success`() = runBlockingTest {
        val eventId = 1
        val event = mock(Event::class.java)
        `when`(getEventByIdUseCase(eventId)).thenReturn(event)

        viewModel.viewModelScope.launch {
            viewModel.getEventById(eventId)
            assertEquals(event, viewModel.event.first())
            assertFalse(viewModel.isLoading.first())
        }
        verify(getEventByIdUseCase).invoke(eventId)
    }

    @Test
    fun `getEventById failure`() = runBlockingTest {
        val eventId = 1
        `when`(getEventByIdUseCase(eventId)).thenThrow(RuntimeException())

        viewModel.viewModelScope.launch {
            viewModel.getEventById(eventId)
            assertNull(viewModel.event.first())
            assertFalse(viewModel.isLoading.first())
        }
        verify(getEventByIdUseCase).invoke(eventId)
    }

    @Test
    fun `createEvent success`() = runBlockingTest {
        val createEventRequest = mock(CreateEventRequest::class.java)
        `when`(createEventUseCase.invoke(createEventRequest)).thenReturn(Result.success(Unit))

        viewModel.viewModelScope.launch {
            viewModel.createEvent(createEventRequest)
            assertFalse(viewModel.isLoading.first())
        }
        verify(createEventUseCase).invoke(createEventRequest)
    }

    @Test
    fun `createEvent failure`() = runBlockingTest {
        val createEventRequest = mock(CreateEventRequest::class.java)
        doThrow(RuntimeException()).`when`(createEventUseCase).invoke(createEventRequest)

        viewModel.viewModelScope.launch {
            viewModel.createEvent(createEventRequest)
            assertFalse(viewModel.isLoading.first())
        }
        verify(createEventUseCase).invoke(createEventRequest)
    }

    @Test
    fun `updateEvent success`() = runBlockingTest {
        val eventId = 1
        val updateEventRequest = mock(UpdateEventRequest::class.java)
        `when`(updateEventUseCase.invoke(eventId, updateEventRequest)).thenReturn(Result.success(Unit))

        viewModel.viewModelScope.launch {
            viewModel.updateEvent(eventId, updateEventRequest)
            assertTrue(viewModel.isLoading.first())
        }
        verify(updateEventUseCase).invoke(eventId, updateEventRequest)
    }

    @Test
    fun `updateEvent failure`() = runBlockingTest {
        val eventId = 1
        val updateEventRequest = mock(UpdateEventRequest::class.java)
        doThrow(RuntimeException()).`when`(updateEventUseCase).invoke(eventId, updateEventRequest)

        viewModel.viewModelScope.launch {
            viewModel.updateEvent(eventId, updateEventRequest)
            assertFalse(viewModel.isLoading.first())
        }
        verify(updateEventUseCase).invoke(eventId, updateEventRequest)
    }

    @Test
    fun `deleteEvent success`() = runBlockingTest {
        val eventId = 1
        `when`(deleteEventUseCase.invoke(eventId)).thenReturn(Result.success(Unit))

        viewModel.viewModelScope.launch {
            viewModel.deleteEvent(eventId)
            assertTrue(viewModel.isLoading.first())
        }
        verify(deleteEventUseCase).invoke(eventId)
    }

    @Test
    fun `deleteEvent failure`() = runBlockingTest {
        val eventId = 1
        doThrow(RuntimeException()).`when`(deleteEventUseCase).invoke(eventId)

        viewModel.viewModelScope.launch {
            viewModel.deleteEvent(eventId)
            assertTrue(viewModel.isLoading.first())
        }
        verify(deleteEventUseCase).invoke(eventId)
    }

    @Test
    fun `joinEvent success`() = runBlockingTest {
        val eventId = 1
        `when`(joinEventUseCase.invoke(eventId)).thenReturn(Result.success(Unit))

        viewModel.viewModelScope.launch {
            viewModel.joinEvent(eventId)
            assertFalse(viewModel.isLoading.first())
        }
        verify(joinEventUseCase).invoke(eventId)
    }

    @Test
    fun `joinEvent failure`() = runBlockingTest {
        val eventId = 1
        doThrow(RuntimeException()).`when`(joinEventUseCase).invoke(eventId)

        viewModel.viewModelScope.launch {
            viewModel.joinEvent(eventId)
            assertFalse(viewModel.isLoading.first())
        }
        verify(joinEventUseCase).invoke(eventId)
    }

    @Test
    fun `leaveEvent success`() = runBlockingTest {
        val eventId = 1
        `when`(leaveEventUseCase.invoke(eventId)).thenReturn(Result.success(Unit))

        viewModel.viewModelScope.launch {
            viewModel.leaveEvent(eventId)
            assertFalse(viewModel.isLoading.first())
        }
        verify(leaveEventUseCase).invoke(eventId)
    }

    @Test
    fun `leaveEvent failure`() = runBlockingTest {
        val eventId = 1
        doThrow(RuntimeException()).`when`(leaveEventUseCase).invoke(eventId)

        viewModel.viewModelScope.launch {
            viewModel.leaveEvent(eventId)
            assertTrue(viewModel.isLoading.first())
        }
        verify(leaveEventUseCase).invoke(eventId)
    }
}