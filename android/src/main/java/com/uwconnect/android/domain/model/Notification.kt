package com.uwconnect.android.domain.model

import java.time.LocalDateTime

data class Notification(
    val id: String,
    val title: String,
    val description: String,
    val dateTime: LocalDateTime,
    val clubName: String
)
