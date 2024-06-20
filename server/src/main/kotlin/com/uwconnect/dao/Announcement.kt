package com.uwconnect.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object Announcements : IntIdTable() {
    val title = varchar("title", 64)
    val description = varchar("description", 256)
    val club = reference("club", Clubs)
    val timestamp = datetime("timestamp")
}

class Announcement(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Announcement>(Announcements)
    var title by Announcements.title
    var description by Announcements.description
    var club by Club referencedOn Announcements.club
    var timestamp by Announcements.timestamp
}