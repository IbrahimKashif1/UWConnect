package com.uwconnect.android.domain.usecase

import com.uwconnect.android.domain.repository.EventRepository
import org.uwconnect.models.UpdateEventRequest

interface UpdateEventUseCase {
    suspend operator fun invoke(eventId: Int, event: UpdateEventRequest): Result<Unit>
}

class UpdateEventUseCaseImpl(private val repository: EventRepository) : UpdateEventUseCase {
    override suspend fun invoke(eventId: Int, event: UpdateEventRequest): Result<Unit> {
        return repository.updateEvent(eventId, event)
    }
}
