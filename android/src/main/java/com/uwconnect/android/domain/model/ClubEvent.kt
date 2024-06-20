package com.uwconnect.android.domain.model

import androidx.annotation.ColorRes
import androidx.compose.ui.graphics.Color
import kotlinx.datetime.LocalDateTime

data class ClubEvent(
    val id: Int,
    val title: String,
    val description: String,
    val location: String?,
    val link: String?,

    val clubName: String,

    val start: LocalDateTime,
    val end: LocalDateTime?,
    val deadline: LocalDateTime?,

    @ColorRes val color: Color,
)