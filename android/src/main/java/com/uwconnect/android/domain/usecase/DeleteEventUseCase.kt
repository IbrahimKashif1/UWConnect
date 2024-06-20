package com.uwconnect.android.domain.usecase

import com.uwconnect.android.domain.repository.EventRepository

interface DeleteEventUseCase {
    suspend operator fun invoke(eventId: Int): Result<Unit>
}

class DeleteEventUseCaseImpl(private val repository: EventRepository) : DeleteEventUseCase {
    override suspend fun invoke(eventId: Int): Result<Unit> = repository.deleteEvent(eventId)
}
