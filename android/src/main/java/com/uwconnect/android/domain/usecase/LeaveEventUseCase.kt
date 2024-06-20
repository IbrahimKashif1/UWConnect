package com.uwconnect.android.domain.usecase

import com.uwconnect.android.domain.repository.EventRepository

interface LeaveEventUseCase {
    suspend operator fun invoke(eventId: Int): Result<Unit>
}

class LeaveEventUseCaseImpl(private val repository: EventRepository) : LeaveEventUseCase {
    override suspend fun invoke(eventId: Int): Result<Unit> = repository.leaveEvent(eventId)
}
