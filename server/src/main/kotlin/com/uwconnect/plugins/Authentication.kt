package com.uwconnect.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureAuthentication() {
    val secret = environment.config.property("jwt.secret").getString()

    install(Authentication) {
        jwt("member-auth") {
            verifier(
                JWT
                .require(Algorithm.HMAC256(secret))
                .build())
            validate { credential ->
                if (credential.payload.getClaim("id").asInt() != null && credential.payload.getClaim("type").asString() == "MEMBER") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }

        jwt("club-auth") {
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .build())
            validate { credential ->
                if (credential.payload.getClaim("id").asInt() != null && credential.payload.getClaim("type").asString() == "CLUB") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }

}