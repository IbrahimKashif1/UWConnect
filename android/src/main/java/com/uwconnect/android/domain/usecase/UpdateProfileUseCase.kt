package com.uwconnect.android.domain.usecase

import com.uwconnect.android.domain.repository.ClubRepository
import org.uwconnect.models.UpdateClubRequest

interface UpdateProfileUseCase {
    suspend operator fun invoke(club: UpdateClubRequest): Result<Unit>
}

class UpdateProfileUseCaseImpl(private val repository: ClubRepository) : UpdateProfileUseCase {
    override suspend fun invoke(club: UpdateClubRequest): Result<Unit> = repository.updateProfile(club)
}
