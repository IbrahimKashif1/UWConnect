package com.uwconnect.android.domain.repository

import org.uwconnect.models.CreateEventRequest
import org.uwconnect.models.Event
import org.uwconnect.models.UpdateEventRequest

interface EventRepository {
    suspend fun getEventById(eventId: Int): Event?

    suspend fun createEvent(event: CreateEventRequest): Result<Unit>

    suspend fun updateEvent(eventId: Int, event: UpdateEventRequest): Result<Unit>

    suspend fun deleteEvent(eventId: Int): Result<Unit>

    suspend fun joinEvent(eventId: Int): Result<Unit>

    suspend fun leaveEvent(eventId: Int): Result<Unit>
}