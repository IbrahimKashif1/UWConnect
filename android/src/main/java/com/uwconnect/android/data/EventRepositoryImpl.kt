package com.uwconnect.android.data

import android.util.Log
import androidx.compose.ui.graphics.Color
import com.uwconnect.android.domain.model.ClubEvent
import com.uwconnect.android.domain.repository.EventRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.uwconnect.models.CreateEventRequest
import org.uwconnect.models.Event
import org.uwconnect.models.UpdateEventRequest
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

class EventRepositoryImpl : EventRepository {
    override suspend fun getEventById(eventId: Int): Event? {
        try {
            val event = coroutineScope {
                async { ApiClient.apiService.getEventById(eventId) }.await().body()?.event
                    ?: throw NoSuchElementException()
            }

            return event
        } catch (e: Exception) {
            return null
        }
    }

    override suspend fun createEvent(event: CreateEventRequest): Result<Unit> {
        try {
            val response = coroutineScope {
                async { ApiClient.apiService.createEvent(event) }.await()
            }

            return if (response.isSuccessful) { Result.success(Unit) } else { Result.failure(Exception()) }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun updateEvent(eventId: Int, event: UpdateEventRequest): Result<Unit> {
        try {
            val response = coroutineScope {
                async { ApiClient.apiService.updateEvent(eventId, event) }.await()
            }

            return if (response.isSuccessful) { Result.success(Unit) } else { Result.failure(Exception()) }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun deleteEvent(eventId: Int): Result<Unit> {
        try {
            val response = coroutineScope {
                async { ApiClient.apiService.deleteEvent(eventId) }.await()
            }

            return if (response.isSuccessful) { Result.success(Unit) } else { Result.failure(Exception()) }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun joinEvent(eventId: Int): Result<Unit> {
        try {
            val response = coroutineScope {
                async { ApiClient.apiService.joinEvent(eventId) }.await()
            }

            return if (response.isSuccessful) { Result.success(Unit) } else { Result.failure(Exception()) }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun leaveEvent(eventId: Int): Result<Unit> {
        try {
            val response = coroutineScope {
                async { ApiClient.apiService.leaveEvent(eventId) }.await()
            }

            return if (response.isSuccessful) { Result.success(Unit) } else { Result.failure(Exception()) }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}