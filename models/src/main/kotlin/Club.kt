package org.uwconnect.models

import kotlinx.serialization.Serializable

@Serializable
data class Club(
    val id: Int,
    var name: String,
    var email: String,
    var description: String?,

    var facebook: String?,
    var instagram: String?,
    var discord: String?,

    var members: List<User>?,
    var events: List<Event>?,
    var announcements: List<Announcement>?,
)
