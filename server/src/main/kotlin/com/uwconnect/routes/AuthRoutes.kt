package com.uwconnect.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.uwconnect.dao.Club
import com.uwconnect.dao.Clubs
import com.uwconnect.dao.User
import com.uwconnect.dao.Users
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.uwconnect.models.GenericResponse
import org.uwconnect.models.LoginRequest
import org.uwconnect.models.LoginResponse
import org.uwconnect.models.SignupRequest
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.transactions.transaction
import org.kotlincrypto.hash.sha2.SHA256
import java.util.*

@OptIn(ExperimentalStdlibApi::class)
fun Route.loginRoute() {
    val secret = environment?.config?.property("jwt.secret")?.getString()

    post("/auth/login") {
        val loginRequest = call.receive<LoginRequest>()

        val dbUser = transaction {
            User.find { Users.email eq loginRequest.email }.singleOrNull()
        }

        application.log.info("dbUser" + dbUser.toString())

        if (dbUser != null) {
            val digest = SHA256()
            digest.update(loginRequest.password.toByteArray(Charsets.UTF_8))
            val hashedPassword = digest.digest().toHexString()

            if (hashedPassword == dbUser.passwordHash) {
                val token = JWT.create()
                    .withClaim("id", dbUser.id.value)
                    .withClaim("type", "MEMBER")
                    .withExpiresAt(Date(System.currentTimeMillis() + 604800000))
                    .sign(Algorithm.HMAC256(secret))

                call.respond(HttpStatusCode.OK, LoginResponse(token)
                )
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        } else {
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun Route.signupRoute() {
    post("/auth/signup") {
        val signupRequest = call.receive<SignupRequest>()

        val dbUser = transaction {
            User.find { Users.email eq signupRequest.email }.singleOrNull()
        }

        if (dbUser == null) {
            val digest = SHA256()
            digest.update(signupRequest.password.toByteArray(Charsets.UTF_8))
            val hashedPassword = digest.digest().toHexString()

            transaction {
                User.new {
                    name = signupRequest.name
                    email = signupRequest.email
                    passwordHash = hashedPassword
                }
            }

            call.respond(HttpStatusCode.Created, GenericResponse("User successfully created"))
        } else {
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun Route.clubLoginRoute() {
    val secret = environment?.config?.property("jwt.secret")?.getString()

    post("/auth/club/login") {
        val loginRequest = call.receive<LoginRequest>()

        val dbClub = transaction {
            Club.find { Clubs.email eq loginRequest.email }.singleOrNull()
        }

        if (dbClub != null) {
            val digest = SHA256()
            digest.update(loginRequest.password.toByteArray(Charsets.UTF_8))
            val hashedPassword = digest.digest().toHexString()

            if (hashedPassword == dbClub.passwordHash) {
                val token = JWT.create()
                    .withClaim("id", dbClub.id.value)
                    .withClaim("type", "CLUB")
                    .withExpiresAt(Date(System.currentTimeMillis() + 604800000))
                    .sign(Algorithm.HMAC256(secret))

                call.respond(HttpStatusCode.OK, LoginResponse(token))
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        } else {
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun Route.clubSignupRoute() {
    post("/auth/club/signup") {
        val signupRequest = call.receive<SignupRequest>()

        val dbClub = transaction {
            Club.find { (Clubs.email eq signupRequest.email) or (Clubs.name eq signupRequest.name) }.singleOrNull()
        }

        if (dbClub == null) {
            val digest = SHA256()
            digest.update(signupRequest.password.toByteArray(Charsets.UTF_8))
            val hashedPassword = digest.digest().toHexString()

            transaction {
                Club.new {
                    name = signupRequest.name
                    email = signupRequest.email
                    passwordHash = hashedPassword
                }
            }

            call.respond(HttpStatusCode.Created, GenericResponse("Club successfully created"))
        } else {
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}

fun Application.authRoutes() {
    routing {
        loginRoute()
        signupRoute()
        clubLoginRoute()
        clubSignupRoute()
    }
}