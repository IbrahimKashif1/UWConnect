package com.uwconnect.android.domain.usecase

import com.uwconnect.android.domain.repository.EventRepository
import org.uwconnect.models.CreateEventRequest

interface CreateEventUseCase {
    suspend operator fun invoke(event: CreateEventRequest): Result<Unit>
}

class CreateEventUseCaseImpl(private val repository: EventRepository) : CreateEventUseCase {
    override suspend fun invoke(event: CreateEventRequest): Result<Unit> {
        return repository.createEvent(event)
    }
}
