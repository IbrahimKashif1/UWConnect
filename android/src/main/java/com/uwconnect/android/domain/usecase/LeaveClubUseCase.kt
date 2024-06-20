package com.uwconnect.android.domain.usecase

import com.uwconnect.android.domain.repository.ClubRepository

interface LeaveClubUseCase {
    suspend operator fun invoke(clubId: Int): Result<Unit>
}

class LeaveClubUseCaseImpl(private val repository: ClubRepository) : LeaveClubUseCase {
    override suspend fun invoke(clubId: Int): Result<Unit> = repository.leaveClub(clubId)
}
