package org.uwconnect.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Announcement(
    val id: Int,
    var title: String,
    var description: String,
    var club: Club,
    var timestamp: LocalDateTime,
)