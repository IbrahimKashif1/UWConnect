package com.uwconnect.android.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uwconnect.android.domain.repository.EventRepository
import com.uwconnect.android.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.uwconnect.models.CreateEventRequest
import org.uwconnect.models.Event
import org.uwconnect.models.UpdateEventRequest
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val getEventByIdUseCase: GetEventByIdUseCase,
    private val createEventUseCase: CreateEventUseCase,
    private val updateEventUseCase: UpdateEventUseCase,
    private val deleteEventUseCase: DeleteEventUseCase,
    private val joinEventUseCase: JoinEventUseCase,
    private val leaveEventUseCase: LeaveEventUseCase
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _event = MutableStateFlow<Event?>(null)
    val event: StateFlow<Event?> = _event

    fun getEventById(eventId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _event.value = getEventByIdUseCase(eventId)
            } catch (e: Exception) {
                _event.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun createEvent(event: CreateEventRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                createEventUseCase(event)
            } catch (_: Exception) {
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun updateEvent(eventId: Int, event: UpdateEventRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                updateEventUseCase(eventId, event)
            } catch (_: Exception) {
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteEvent(eventId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                deleteEventUseCase(eventId)
            } catch (_: Exception) {
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun joinEvent(eventId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                joinEventUseCase(eventId)
            } catch (_: Exception) {
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun leaveEvent(eventId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                leaveEventUseCase(eventId)
            } catch (_: Exception) {
            } finally {
                _isLoading.value = false
            }
        }
    }
}