package com.uwconnect.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object ClubUsers : IntIdTable() {
    val club = reference("club", Clubs, onDelete = ReferenceOption.CASCADE)
    val user = reference("user", Users, onDelete = ReferenceOption.CASCADE)
}

object EventUsers : IntIdTable() {
    val event = reference("event", Events, onDelete = ReferenceOption.CASCADE)
    val user = reference("user", Users, onDelete = ReferenceOption.CASCADE)
}

class EventUser(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<EventUser>(EventUsers)
    var event by EventUsers.event
    var user by EventUsers.user
}