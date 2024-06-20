package com.uwconnect.utils

import com.uwconnect.dao.Event
import com.uwconnect.dao.Notification as NotificationDAO
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.uwconnect.dao.EventUser
import com.uwconnect.dao.EventUsers
import com.uwconnect.dao.User
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.transactions.transaction

data class NotificationContent(
    val token: String,
    val title: String,
    val body: String
)

fun sendNotification(notification: NotificationContent) {
    val message = Message.builder()
        .setNotification(
            Notification.builder()
                .setTitle(notification.title)
                .setBody(notification.body)
                .build()
        )
        .apply {
            setToken(notification.token)
        }
        .build()

    FirebaseMessaging.getInstance().send(message)
}


fun sendNotifications(notifications: List<NotificationContent>) {
    notifications.forEach {
        sendNotification(it)
    }
}

fun sendUpcomingEventNotifications() {
    // get current timestamp
    val now = Clock.System.now()

    println("STARTING EVENT NOTIFICATIONS TASK...")

    // fetch all upcoming events
    var events = transaction {
        Event.all().with(Event::participants).map { it }
    }

    // fetch all sent notifications
    val notifications = transaction {
        NotificationDAO.all().map { it }
    }

    events = events.filter { (it.start.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds() + 14400000) >= now.toLocalDateTime(TimeZone.currentSystemDefault()).toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds() }

    events.forEach { event ->
        val timeDifference = event.start.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds() + 14400000 - now.toEpochMilliseconds()

        // check if less than 15 minutes away
        if (timeDifference in 780000..900000) {
            println("SENDING 15M NOTIFICATIONS FOR EVENT: $event")

            val participants = transaction {
                EventUser.find { EventUsers.event eq event.id }.map { it.user.value }
            }

            println("EVENT PARTICIPANTS: $participants")

            participants.forEach { eventUserId ->
                val user = transaction {
                    User.findById(eventUserId)
                }

                if (user != null && !user.deviceToken.isNullOrBlank() && !notifications.any { it.eventId == event.id.value && it.userId == user.id.value && it.type == "15" }) {
                    println("SENDING NOTIFICATION to $user")
                    try {
                        sendNotification(NotificationContent(
                            user.deviceToken!!,
                            "Upcoming Event: " + event.title,
                            event.title + " is happening in less than 15 minutes!"
                        ))
                        transaction {
                            NotificationDAO.new {
                                userId = user.id.value
                                eventId = event.id.value
                                type = "15"
                            }
                        }
                    } catch (e: Exception) {
                        println(e.toString())
                    }
                }
            }
        }


        // check if less than 12 hours away
        if (timeDifference in 43080000..43200000) {
            println("SENDING 12H NOTIFICATIONS FOR EVENT: $event")

            val participants = transaction {
                EventUser.find { EventUsers.event eq event.id }.map { it.user.value }
            }

            println("EVENT PARTICIPANTS: $participants")

            participants.forEach { eventUserId ->
                val user = transaction {
                    User.findById(eventUserId)
                }

                if (user != null && !user.deviceToken.isNullOrBlank() && !notifications.any { it.eventId == event.id.value && it.userId == user.id.value && it.type == "12" }) {
                    println("SENDING NOTIFICATION to $user")
                    try {
                        sendNotification(NotificationContent(
                            user.deviceToken!!,
                            "Upcoming Event: " + event.title,
                            event.title + " is happening in less than 12 hours!"
                        ))
                        transaction {
                            NotificationDAO.new {
                                userId = user.id.value
                                eventId = event.id.value
                                type = "12"
                            }
                        }
                    } catch (e: Exception) {
                        println(e.toString())
                    }
                }
            }
        }
    }
}
