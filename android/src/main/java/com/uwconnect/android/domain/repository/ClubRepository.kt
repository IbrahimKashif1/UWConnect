package com.uwconnect.android.domain.repository

import org.uwconnect.models.Club
import org.uwconnect.models.CreateAnnouncementRequest
import org.uwconnect.models.UpdateClubRequest

interface ClubRepository {
    suspend fun getProfile(): Club?

    suspend fun getProfileById(clubId: Int): Club?

    suspend fun getAll(): List<Club>

    suspend fun updateProfile(club: UpdateClubRequest): Result<Unit>

    suspend fun joinClub(clubId: Int): Result<Unit>

    suspend fun leaveClub(clubId: Int): Result<Unit>

    suspend fun createAnnouncement(announcement: CreateAnnouncementRequest): Result<Unit>

    suspend fun validateClubName(clubName: String): Result<Unit>
}