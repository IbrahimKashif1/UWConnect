package com.uwconnect.routes

import com.uwconnect.dao.Club
import com.uwconnect.utils.*
import com.uwconnect.dao.User as UserDAO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.uwconnect.models.*
import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class UserEventsResponse(
    val events: List<Event>
)

@Serializable
data class UserUpdatesResponse(
    val announcements: List<Announcement>
)

fun Route.userProfileRoutes() {
    authenticate ("member-auth") {
        get("/user/profile") {
            val principal = call.principal<JWTPrincipal>()
            val id = principal!!.payload.getClaim("id").asInt()

            val userData = transaction {
                UserDAO.findById(id)?.load(UserDAO::clubs)?.load(UserDAO::events)
            }

            if (userData == null) {
                call.respond(HttpStatusCode.InternalServerError)
            } else {
                val user = userMapper(userData)

                user.clubs = clubsMapper(userData.clubs)
                user.events = transaction { eventsMapper(userData.events) }
                call.respond(HttpStatusCode.OK, UserResponse(user))
            }
        }
    }

    get("/user/profile/{id}") {
        val idParam = call.parameters["id"]
        val userId = idParam?.toIntOrNull()

        if (userId == null) {
            call.respond(HttpStatusCode.BadRequest)
        } else {
            val userData = transaction {
                UserDAO.findById(userId)
            }

            if (userData == null) {
                call.respond(HttpStatusCode.InternalServerError)
            } else {
                val user = userMapper(userData)
                call.respond(HttpStatusCode.OK, UserResponse(user))
            }
        }
    }
}

fun Route.userEventsRoute() {
    authenticate ("member-auth") {
        get("/user/events") {
            val principal = call.principal<JWTPrincipal>()
            val id = principal!!.payload.getClaim("id").asInt()

            val userData = transaction {
                UserDAO.findById(id)?.load(UserDAO::events)
            }

            if (userData == null) {
                call.respond(HttpStatusCode.InternalServerError)
            } else {
                val events = eventsMapper(userData.events)
                call.respond(HttpStatusCode.OK, UserEventsResponse(events))
            }
        }
    }
}

fun Route.userAnnouncementsRoute() {
    authenticate ("member-auth") {
        get("/user/announcements") {
            val principal = call.principal<JWTPrincipal>()
            val id = principal!!.payload.getClaim("id").asInt()

            val userData = transaction {
                UserDAO.findById(id)?.load(UserDAO::clubs)?.load(Club::announcements)
            }

            if (userData == null) {
                call.respond(HttpStatusCode.InternalServerError)
            } else {
                val announcements = transaction { userData.clubs.map { it.announcements.map {announcementMapper(it)} }.flatten() }
                call.application.log.info("announcements: " + announcements)
                call.respond(HttpStatusCode.OK, AnnouncementsResponse(announcements))
            }
        }
    }
}

fun Route.updateUserRoute() {
    authenticate ("member-auth") {
        post("/user/update") {
            val principal = call.principal<JWTPrincipal>()
            val id = principal!!.payload.getClaim("id").asInt()

            val updateUserRequest = call.receive<UpdateUserRequest>()

            val userData = transaction {
                UserDAO.findById(id)
            }

            if (userData == null) {
                call.respond(HttpStatusCode.InternalServerError)
            } else {
                transaction {
                    userData.name = updateUserRequest.name
                    userData.bio = updateUserRequest.bio
                }
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}

fun Route.registerTokenRoute() {
    authenticate("member-auth") {
        post("/user/token") {
            val principal = call.principal<JWTPrincipal>()
            val id = principal!!.payload.getClaim("id").asInt()

            val registerTokenRequest = call.receive<RegisterTokenRequest>()

            val userData = transaction {
                UserDAO.findById(id)
            }

            if (userData == null) {
                call.respond(HttpStatusCode.InternalServerError)
            } else {
                transaction {
                    userData.deviceToken = registerTokenRequest.token
                }
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}

fun Application.userRoutes() {
    routing {
        userProfileRoutes()
        userEventsRoute()
        userAnnouncementsRoute()
        updateUserRoute()
        registerTokenRoute()
    }
}