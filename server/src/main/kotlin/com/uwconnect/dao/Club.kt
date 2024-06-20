package com.uwconnect.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Clubs : IntIdTable() {
    val name = varchar("name", 64)
    val email = varchar("email", 128).uniqueIndex()
    val description = varchar("description", 256).nullable()
    val passwordHash = varchar("password_hash", 128)

    val instagram = varchar("instagram", 64).nullable()
    val discord = varchar("discord", 64).nullable()
    val facebook = varchar("facebook", 64).nullable()
}

class Club(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Club>(Clubs)
    var name     by Clubs.name
    var email by Clubs.email
    var description by Clubs.description
    var passwordHash by Clubs.passwordHash

    var facebook by Clubs.facebook
    var instagram by Clubs.instagram
    var discord by Clubs.discord

    var members by User via ClubUsers
    val events by Event referrersOn Events.club
    val announcements by Announcement referrersOn Announcements.club
}