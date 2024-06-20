package com.uwconnect.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Notifications : IntIdTable() {
    val eventId = integer("event_id")
    val userId = integer("user_id")
    val type = varchar("type", 16)
}

class Notification(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Notification>(Notifications)
    var eventId by Notifications.eventId
    var userId by Notifications.userId
    var type by Notifications.type
}