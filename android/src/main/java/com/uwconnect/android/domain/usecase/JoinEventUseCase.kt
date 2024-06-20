package com.uwconnect.android.domain.usecase

import com.uwconnect.android.domain.repository.EventRepository

interface JoinEventUseCase {
    suspend operator fun invoke(eventId: Int): Result<Unit>
}

class JoinEventUseCaseImpl(private val repository: EventRepository) : JoinEventUseCase {
    override suspend fun invoke(eventId: Int): Result<Unit> = repository.joinEvent(eventId)
}
