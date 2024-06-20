package com.uwconnect.routes

import com.uwconnect.dao.*
import com.uwconnect.dao.Announcement
import com.uwconnect.dao.Event
import com.uwconnect.dao.User
import com.uwconnect.utils.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.apache.http.HttpStatus
import org.uwconnect.models.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import com.uwconnect.dao.Club as ClubDAO

fun Route.allClubsRoute() {
    get("/club/all") {
        val clubData = transaction {
            ClubDAO.all().with(ClubDAO::members) // .filter { it.visibility == "public" }
        }

        val clubs = transaction {
            val clubs = clubsMapper(clubData)
            clubs.forEach { club ->
                club.members = usersMapper(clubData.find{ it.id.value == club.id }!!.members)
            }
            clubs
        }

        call.respond(HttpStatusCode.OK, ClubsResponse(clubs))
    }
}

fun Route.clubProfileRoutes() {
    authenticate ("club-auth") {
        get("/club/profile") {
            val principal = call.principal<JWTPrincipal>()
            val id = principal!!.payload.getClaim("id").asInt()

            val clubData = transaction {
                ClubDAO.findById(id)?.load(ClubDAO::members)
            }

            if (clubData == null) {
                call.respond(HttpStatusCode.NotFound, message = "User not found")
            } else {
                val club = clubMapper(clubData)

                val events = transaction {
                    eventsMapper(Event.find { Events.club eq id })
                }

                val announcements = transaction {
                    announcementsMapper(Announcement.find { Announcements.club eq id })
                }

                club.members = usersMapper(clubData.members)
                club.events = events
                club.announcements = announcements

                call.respond(HttpStatusCode.OK, ClubResponse(club))
            }
        }
    }

    get("/club/profile/{id}") {
        val idParam = call.parameters["id"]
        val id = idParam?.toIntOrNull()

        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Must provide club id")
        } else {
            val clubData = transaction {
                ClubDAO.findById(id)?.load(ClubDAO::members)
            }

            if (clubData == null) {
                call.respond(HttpStatusCode.InternalServerError)
            } else {
                val club = clubMapper(clubData)

                val events = transaction {
                    eventsMapper(Event.find { Events.club eq id })
                }

                val announcements = transaction {
                    announcementsMapper(Announcement.find { Announcements.club eq id })
                }

                club.members = usersMapper(clubData.members)
                club.events = events
                club.announcements = announcements

                call.respond(HttpStatusCode.OK, ClubResponse(club))
            }
        }
    }
}

fun Route.joinClubRoute() {
    authenticate("member-auth") {
        post("/club/{id}/join") {
            val idParam = call.parameters["id"]
            val clubId = idParam?.toIntOrNull()

            if (clubId == null) {
                call.respond(HttpStatusCode.BadRequest)
            } else {
                val club = transaction {
                    ClubDAO.findById(clubId)?.load(ClubDAO::members)
                }

                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("id").asInt()

                val user = transaction {
                    User.findById(userId)
                }

                if (club == null || user == null) {
                    call.respond(HttpStatusCode.InternalServerError)
                } else if (club.members.map {it.id}.contains(user.id)) {
                    call.respond(HttpStatusCode.BadRequest)
                } else {
                    transaction {
                        ClubUsers.insert {
                            it[ClubUsers.club] = EntityID(clubId, Clubs)
                            it[ClubUsers.user] = EntityID(userId, Users)
                        }
                    }

                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}

fun Route.leaveClubRoute() {
    authenticate("member-auth") {
        post("/club/{id}/leave") {
            val idParam = call.parameters["id"]
            val clubId = idParam?.toIntOrNull()

            if (clubId == null) {
                call.respond(HttpStatusCode.BadRequest)
            } else {
                val club = transaction {
                    ClubDAO.findById(clubId)
                }

                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("id").asInt()

                val user = transaction {
                    User.findById(userId)
                }

                if (club == null || user == null) {
                    call.respond(HttpStatusCode.InternalServerError)
                } else {
                    transaction {
                        ClubUsers.deleteWhere {
                            (ClubUsers.user eq userId) and (ClubUsers.club eq clubId)
                        }
                    }

                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}

fun Route.updateClubRoute() {
    authenticate("club-auth") {
        post("/club/update") {
            val principal = call.principal<JWTPrincipal>()
            val id = principal!!.payload.getClaim("id").asInt()

            val updateClubRequest = call.receive<UpdateClubRequest>()

            val clubData = transaction {
                ClubDAO.findById(id)
            }

            if (clubData == null) {
                call.respond(HttpStatusCode.InternalServerError)
            } else {
                val existingClubNameCount = transaction { Clubs.selectAll().where { (Clubs.name eq clubData.name) and (Clubs.id neq clubData.id) }.count().toInt() }

                if (existingClubNameCount == 0) {
                    transaction {
                        clubData.name = updateClubRequest.name
                        clubData.description = updateClubRequest.description
                        clubData.facebook = updateClubRequest.facebook
                        clubData.instagram = updateClubRequest.instagram
                        clubData.discord = updateClubRequest.discord
                    }

                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }
    }
}

fun Route.createAnnouncementRoute() {
    authenticate("club-auth") {
        post("/club/create-announcement") {
            val principal = call.principal<JWTPrincipal>()
            val id = principal!!.payload.getClaim("id").asInt()

            val createAnnouncementRequest = call.receive<CreateAnnouncementRequest>()

            val clubData = transaction {
                ClubDAO.findById(id)?.load(ClubDAO::members)
            }

            if (clubData == null) {
                call.respond(HttpStatusCode.InternalServerError)
            } else {
                // create the announcement and send a notification to each member in the club

                transaction {
                    Announcement.new {
                        title = createAnnouncementRequest.title
                        description = createAnnouncementRequest.description
                        timestamp = createAnnouncementRequest.timestamp
                        club = clubData
                    }
                }

                val notifications = clubData.members.mapNotNull { it.deviceToken }.map {NotificationContent(
                    it, clubData.name + " posted a new announcement: " + createAnnouncementRequest.title, createAnnouncementRequest.description
                )}

                sendNotifications(notifications)

                call.respond(HttpStatusCode.OK)
            }
        }
    }
}

fun Application.clubRoutes() {
    routing {
        allClubsRoute()
        clubProfileRoutes()
        joinClubRoute()
        leaveClubRoute()
        updateClubRoute()
        createAnnouncementRoute()
    }
}