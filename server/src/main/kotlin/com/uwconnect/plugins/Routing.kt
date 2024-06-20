package com.uwconnect.plugins

import com.uwconnect.routes.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    authRoutes()
    userRoutes()
    clubRoutes()
    eventRoutes()
}
