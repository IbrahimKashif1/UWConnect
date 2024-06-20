package com.uwconnect.android.domain.usecase

import com.uwconnect.android.domain.repository.ClubRepository
import org.uwconnect.models.CreateAnnouncementRequest

interface CreateClubAnnouncementUseCase {
    suspend operator fun invoke(announcement: CreateAnnouncementRequest): Result<Unit>
}

class CreateClubAnnouncementUseCaseImpl(private val repository: ClubRepository) : CreateClubAnnouncementUseCase {
    override suspend fun invoke(announcement: CreateAnnouncementRequest): Result<Unit> {
        return repository.createAnnouncement(announcement)
    }
}
