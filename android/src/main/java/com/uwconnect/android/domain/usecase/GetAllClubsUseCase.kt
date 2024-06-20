package com.uwconnect.android.domain.usecase

import com.uwconnect.android.domain.repository.ClubRepository
import org.uwconnect.models.Club

interface GetAllClubsUseCase {
    suspend operator fun invoke(): List<Club>
}

class GetAllClubsUseCaseImpl(private val repository: ClubRepository) : GetAllClubsUseCase {
    override suspend fun invoke(): List<Club> = repository.getAll()
}
