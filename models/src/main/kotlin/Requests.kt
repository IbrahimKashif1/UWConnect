package org.uwconnect.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class SignupRequest(
    val name: String,
    val email: String,
    val password: String
)

@Serializable
data class UpdateClubRequest(
    val name: String,
    val description: String,
    val facebook: String?,
    val instagram: String?,
    val discord: String?,
)

@Serializable
data class CreateEventRequest(
    val title: String,
    val description: String,
    val location: String?,
    val link: String?,
    val color: String,
    val start: LocalDateTime,
    val end: LocalDateTime,
)

@Serializable
data class UpdateEventRequest(
    val title: String,
    val description: String,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val location: String?,
    val link: String?,
    val color: String,
)

@Serializable
data class CreateAnnouncementRequest(
    val title: String,
    val description: String,
    val timestamp: LocalDateTime,
)

@Serializable
data class UpdateUserRequest(
    val name: String,
    val bio: String,
)

@Serializable
data class RegisterTokenRequest(
    val token: String
)