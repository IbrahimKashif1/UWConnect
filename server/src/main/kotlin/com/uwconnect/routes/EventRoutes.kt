package com.uwconnect.routes

import com.uwconnect.dao.EventUsers
import com.uwconnect.dao.Events
import com.uwconnect.dao.Users
import com.uwconnect.utils.*
import com.uwconnect.dao.Club as ClubDAO
import com.uwconnect.dao.Event as EventDAO
import com.uwconnect.dao.User as UserDAO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.uwconnect.models.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.getEventRoute() {
    get("/event/{id}") {
        val idParam = call.parameters["id"]
        val eventId = idParam?.toIntOrNull()

        if (eventId == null) {
            call.respond(HttpStatusCode.BadRequest)
        } else {
            val eventData = transaction {
                EventDAO.findById(eventId)
            }

            if (eventData == null) {
                call.respond(HttpStatusCode.InternalServerError)
            } else {
                val event = transaction {
                    eventMapper(eventData)
                }
                call.respond(HttpStatusCode.OK, EventResponse(event))
            }
        }
    }
}

fun Route.createEventRoute() {
    authenticate("club-auth") {
        post("/event/create") {
            val principal = call.principal<JWTPrincipal>()
            val id = principal!!.payload.getClaim("id").asInt()

            val createEventRequest = call.receive<CreateEventRequest>()

            val clubData = transaction {
                ClubDAO.findById(id)?.load(ClubDAO::members)
            }

            if (clubData == null) {
                call.respond(HttpStatusCode.InternalServerError)
            } else {
                val newEvent = transaction {
                    EventDAO.new {
                        title = createEventRequest.title
                        description = createEventRequest.description
                        club = clubData
                        start = createEventRequest.start
                        end = createEventRequest.end
                        location = createEventRequest.location
                        link = createEventRequest.link
                        color = createEventRequest.color
                    }
                }

                // send a notification to all club members
                val notifications = clubData.members.mapNotNull { it.deviceToken }.map {NotificationContent(
                    it, "New Event: " + newEvent.title,clubData.name + " has posted a new event: " + newEvent.description
                )}

                sendNotifications(notifications)

                call.respond(HttpStatusCode.OK)
            }
        }
    }
}

fun Route.updateEventRoute() {
    authenticate("club-auth") {
        post("/event/{id}/update") {
            val principal = call.principal<JWTPrincipal>()
            val id = principal!!.payload.getClaim("id").asInt()

            val idParam = call.parameters["id"]
            val eventId = idParam?.toIntOrNull()

            val updateEventRequest = call.receive<UpdateEventRequest>()

            if (eventId == null) {
                call.respond(HttpStatusCode.BadRequest)
            } else {
                val eventData = transaction {
                    EventDAO.findById(eventId)
                }

                if (eventData == null) {
                    call.respond(HttpStatusCode.InternalServerError)
                } else if (transaction { eventData.club.id.value != id }) {
                    call.respond(HttpStatusCode.Forbidden)
                } else {
                    transaction {
                        eventData.title = updateEventRequest.title
                        eventData.description = updateEventRequest.description
                        eventData.start = updateEventRequest.start
                        eventData.end = updateEventRequest.end
                        eventData.location = updateEventRequest.location
                        eventData.link = updateEventRequest.link
                        eventData.color = updateEventRequest.color
                    }
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}

fun Route.deleteEventRoute() {
    authenticate("club-auth") {
        post("/event/{id}/delete") {
            val principal = call.principal<JWTPrincipal>()
            val id = principal!!.payload.getClaim("id").asInt()

            val idParam = call.parameters["id"]
            val eventId = idParam?.toIntOrNull()

            if (eventId == null) {
                call.respond(HttpStatusCode.BadRequest)
            } else {
                val clubData = transaction {
                    ClubDAO.findById(id)
                }

                if (clubData == null) {
                    call.respond(HttpStatusCode.InternalServerError)
                } else {
                    val eventData = transaction {
                        EventDAO.findById(eventId)
                    }

                    if (eventData == null) {
                        call.respond(HttpStatusCode.InternalServerError)
                    } else if (transaction{ eventData.club.id.value != id }) {
                        call.respond(HttpStatusCode.Forbidden)
                    } else {
                        transaction {
                            eventData.delete()
                        }
                        call.respond(HttpStatusCode.OK)
                    }
                }
            }
        }
    }
}

fun Route.joinEventRoute() {
    authenticate("member-auth") {
        post("/event/{id}/join") {
            val principal = call.principal<JWTPrincipal>()
            val id = principal!!.payload.getClaim("id").asInt()

            val idParam = call.parameters["id"]
            val eventId = idParam?.toIntOrNull()

            if (eventId == null) {
                call.respond(HttpStatusCode.BadRequest)
            } else {
                val eventData = transaction {
                    EventDAO.findById(eventId)?.load(EventDAO::participants)
                }

                val userData = transaction {
                    UserDAO.findById(id)
                }

                if (eventData == null || userData == null) {
                    call.respond(HttpStatusCode.InternalServerError)
                } else if (eventData.participants.contains(userData)) {
                    call.respond(HttpStatusCode.BadRequest)
                } else {
                    transaction {
                        EventUsers.insert {
                            it[event] = EntityID(eventId, Events)
                            it[user] = EntityID(id, Users)
                        }
                    }

                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}

fun Route.leaveEventRoute() {
    authenticate("member-auth") {
        post("/event/{id}/leave") {
            val principal = call.principal<JWTPrincipal>()
            val id = principal!!.payload.getClaim("id").asInt()

            val idParam = call.parameters["id"]
            val eventId = idParam?.toIntOrNull()

            if (eventId == null) {
                call.respond(HttpStatusCode.BadRequest)
            } else {
                val eventData = transaction {
                    EventDAO.findById(eventId)?.load(EventDAO::participants)
                }

                val userData = transaction {
                    UserDAO.findById(id)
                }

                if (eventData == null || userData == null) {
                    call.respond(HttpStatusCode.InternalServerError)
                } else {
                    transaction {
                        EventUsers.deleteWhere {
                            (user eq id) and (event eq eventId)
                        }
                    }

                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}

fun Application.eventRoutes() {
    routing {
        getEventRoute()
        createEventRoute()
        updateEventRoute()
        deleteEventRoute()
        joinEventRoute()
        leaveEventRoute()
    }
}