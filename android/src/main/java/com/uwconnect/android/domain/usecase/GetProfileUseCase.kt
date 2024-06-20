package com.uwconnect.android.domain.usecase

import com.uwconnect.android.domain.repository.ClubRepository
import org.uwconnect.models.Club

interface GetProfileUseCase {
    suspend operator fun invoke(): Club?
}

class GetProfileUseCaseImpl(private val repository: ClubRepository) : GetProfileUseCase {
    override suspend fun invoke(): Club? = repository.getProfile()
}
