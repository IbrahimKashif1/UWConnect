package com.uwconnect.android.data

import com.uwconnect.android.domain.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.uwconnect.models.Announcement
import org.uwconnect.models.UpdateUserRequest
import org.uwconnect.models.User

class UserRepositoryImpl: UserRepository {
    override suspend fun getProfile(): User? {
        try {
            val user = coroutineScope {
                async { ApiClient.apiService.getUserProfile() }.await().body()?.user
                    ?: throw NoSuchElementException("Could not retrieve user")
            }

            return user
        } catch (e: Exception) {
            return null
        }
    }

    override suspend fun updateProfile(user: UpdateUserRequest): Result<Unit> {
        try {
            val response = coroutineScope {
                async { ApiClient.apiService.updateUserProfile(user) }.await()
            }

            return if (response.isSuccessful) Result.success(Unit) else Result.failure(Exception())
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun getAnnouncements(): List<Announcement> {
        try {
            val announcements = coroutineScope {
                async { ApiClient.apiService.getUserAnnouncements() }.await().body()?.announcements
                    ?: throw NoSuchElementException("Could not retrieve announcements")
            }

            return announcements
        } catch (e: Exception) {
            return emptyList()
        }
    }
}
