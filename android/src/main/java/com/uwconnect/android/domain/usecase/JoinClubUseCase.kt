package com.uwconnect.android.domain.usecase

import com.uwconnect.android.domain.repository.ClubRepository

interface JoinClubUseCase {
    suspend operator fun invoke(clubId: Int): Result<Unit>
}

class JoinClubUseCaseImpl(private val repository: ClubRepository) : JoinClubUseCase {
    override suspend fun invoke(clubId: Int): Result<Unit> = repository.joinClub(clubId)
}
