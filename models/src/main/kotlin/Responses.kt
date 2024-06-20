package org.uwconnect.models

import kotlinx.serialization.Serializable

@Serializable
data class GenericResponse(
    val message: String
)

@Serializable
data class LoginResponse(
    val jwt: String
)

@Serializable
data class SignupResponse(
    val msg: String
)

@Serializable
data class ClubResponse(
    val club: Club
)

@Serializable
data class ClubsResponse(
    val clubs: List<Club>
)

@Serializable
data class UserResponse(
    val user: User
)

@Serializable
data class EventResponse(
    val event: Event
)

@Serializable
data class AnnouncementsResponse(
    val announcements: List<Announcement>
)