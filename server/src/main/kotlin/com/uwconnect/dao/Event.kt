package com.uwconnect.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object Events : IntIdTable() {
    val title = varchar("title", 64)
    val description = varchar("description", 256)
    val location = varchar("location", 64).nullable()
    val link = varchar("link", 128).nullable()
    val color = varchar("color", 64)

    val club = reference("club", Clubs)

    val start = datetime("start")
    val end = datetime("end")
}

class Event(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Event>(Events)
    var title by Events.title
    var description by Events.description
    var location by Events.location
    var link by Events.link
    var color by Events.color

    var club by Club referencedOn Events.club

    var start by Events.start
    var end by Events.end

    var participants by User via EventUsers
}