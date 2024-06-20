package org.uwconnect.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    var name: String,
    var email: String,
    var bio: String?,

    var clubs: List<Club>?,
    var events: List<Event>?,
)