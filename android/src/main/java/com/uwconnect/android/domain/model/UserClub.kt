package com.uwconnect.android.domain.model

data class UserClub(
    val id: Int,
    val name: String,
    val email: String,
    val description: String?,
    val visibility: String,

    val facebook: String?,
    val instagram: String?,
    val discord: String?,

    var isSubscribed: Boolean?
)