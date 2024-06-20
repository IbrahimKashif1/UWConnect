package org.uwconnect.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val id: Int,
    var title: String,
    var description: String,
    var location: String?,
    var link: String?,
    var color: String,

    var club: Club,

    var start: LocalDateTime,
    var end: LocalDateTime,

    val participants: List<User>?,
)