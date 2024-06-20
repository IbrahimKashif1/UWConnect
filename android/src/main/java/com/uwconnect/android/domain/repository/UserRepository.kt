package com.uwconnect.android.domain.repository

import org.uwconnect.models.Announcement
import org.uwconnect.models.UpdateUserRequest
import org.uwconnect.models.User

interface UserRepository {
    suspend fun getProfile(): User?

    suspend fun updateProfile(user: UpdateUserRequest): Result<Unit>

    suspend fun getAnnouncements(): List<Announcement>
}