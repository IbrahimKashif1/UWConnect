package com.uwconnect.android.data

import android.util.Log
import com.uwconnect.android.domain.repository.ClubRepository
import com.uwconnect.android.util.TokenManager
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.uwconnect.models.Club
import org.uwconnect.models.CreateAnnouncementRequest
import org.uwconnect.models.UpdateClubRequest

class ClubRepositoryImpl : ClubRepository {
    override suspend fun getProfile(): Club? {
        try {
            val club = coroutineScope {
                async {
                    ApiClient.apiService.getClubProfile()
                }.await().body()?.club
                    ?: throw NoSuchElementException("Club not found")
            }
            return club
        } catch (e: Exception) {
            return null
        }
    }

    override suspend fun getProfileById(clubId: Int): Club? {
        try {
            val club = coroutineScope {
                async {
                    ApiClient.apiService.getClubByID(clubId)
                }.await().body()?.club
                    ?: throw NoSuchElementException("Club with ID $clubId not found")
            }
            return club
        } catch (e: Exception) {
            return null
        }
    }

    override suspend fun getAll(): List<Club> {
        try {
            val clubs = coroutineScope {
                async {
                    ApiClient.apiService.getAllClubs()
                }.await().body()?.clubs
                    ?: throw NoSuchElementException("Clubs not found")
            }
            return clubs
        } catch (e: Exception) {
            return emptyList()
        }
    }

    override suspend fun joinClub(clubId: Int): Result<Unit> {
        try {
            val response = coroutineScope {
                async {
                    ApiClient.apiService.joinClub(clubId)
                }.await()
            }

            return if (response.isSuccessful) { Result.success(Unit) } else { Result.failure(Exception()) }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun leaveClub(clubId: Int): Result<Unit> {
        try {
            val response = coroutineScope {
                async {
                    ApiClient.apiService.leaveClub(clubId)
                }.await()
            }

            return if (response.isSuccessful) { Result.success(Unit) } else { Result.failure(Exception()) }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun updateProfile(club: UpdateClubRequest): Result<Unit> {
        try {
            val response = coroutineScope {
                async {
                    ApiClient.apiService.updateClubProfile(club)
                }.await()
            }
            return if (response.isSuccessful) { Result.success(Unit) } else { Result.failure(Exception()) }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun createAnnouncement(announcement: CreateAnnouncementRequest): Result<Unit> {
        try {
            val response = coroutineScope {
                async {
                    ApiClient.apiService.createClubAnnouncement(announcement)
                }.await()
            }
            return if (response.isSuccessful) { Result.success(Unit) } else { Result.failure(Exception()) }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun validateClubName(clubName: String): Result<Unit> {
        try {
            if (clubName.isBlank()) {
                return Result.failure(Exception())
            }

            val allClubs = coroutineScope {
                async {
                    ApiClient.apiService.getAllClubs()
                }.await().body()?.clubs
                    ?: emptyList()
            }

            return if (!allClubs.any {
                    it.id != TokenManager.parseJwt(TokenManager.getJwt()).id && it.name.equals(clubName, true)
                }) { Result.success(Unit) } else {Result.failure(Exception()) }
        } catch (e: Exception) {
            return Result.failure(Exception())
        }
    }
}