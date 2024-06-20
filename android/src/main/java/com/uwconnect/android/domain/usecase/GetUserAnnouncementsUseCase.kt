package com.uwconnect.android.domain.usecase

import com.uwconnect.android.domain.repository.UserRepository
import org.uwconnect.models.Announcement

interface GetUserAnnouncementsUseCase {
    suspend operator fun invoke(): List<Announcement>
}

class GetUserAnnouncementUseCaseImpl(private val repository: UserRepository) : GetUserAnnouncementsUseCase {
    override suspend fun invoke(): List<Announcement> {
        return repository.getAnnouncements()
    }
}
