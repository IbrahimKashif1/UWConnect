package com.uwconnect.android.domain.usecase

import com.uwconnect.android.domain.repository.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.uwconnect.models.Event

interface GetEventByIdUseCase {
    suspend operator fun invoke(eventId: Int): Event?
}

class GetEventByIdUseCaseImpl(private val repository: EventRepository) : GetEventByIdUseCase {
    override suspend fun invoke(eventId: Int): Event? = withContext(Dispatchers.IO) {
        repository.getEventById(eventId)
    }
}
