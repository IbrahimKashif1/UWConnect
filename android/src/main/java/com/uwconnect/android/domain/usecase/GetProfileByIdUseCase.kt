package com.uwconnect.android.domain.usecase

import com.uwconnect.android.domain.repository.ClubRepository
import org.uwconnect.models.Club

interface GetProfileByIdUseCase {
    suspend operator fun invoke(clubId: Int): Club?
}

class GetProfileByIdUseCaseImpl(private val repository: ClubRepository) : GetProfileByIdUseCase {
    override suspend fun invoke(clubId: Int): Club? = repository.getProfileById(clubId)
}
