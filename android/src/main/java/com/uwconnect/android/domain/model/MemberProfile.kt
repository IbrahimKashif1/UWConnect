package com.uwconnect.android.domain.model

data class MemberProfile(
    val id: Int,
    val name: String,
    val email: String,
    val bio: String?
)