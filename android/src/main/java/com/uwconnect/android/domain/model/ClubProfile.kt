package com.uwconnect.android.domain.model

data class ClubProfile(
    val id: Int,
    val name: String,
    val description: String?,
    val visibility: String,
    val facebook: String?,
    val instagram: String?,
    val discord: String?,
)