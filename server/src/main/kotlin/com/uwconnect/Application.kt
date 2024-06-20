package com.uwconnect

import com.uwconnect.dao.DatabaseSingleton
import com.uwconnect.plugins.configureAuthentication
import com.uwconnect.plugins.configureHTTP
import com.uwconnect.plugins.configureRouting
import com.uwconnect.plugins.configureSerialization
import com.uwconnect.utils.sendUpcomingEventNotifications
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ktor.server.application.*
import io.ktor.server.netty.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    val databaseURL = environment.config.property("database.url").getString()
    val databaseUser = environment.config.property("database.user").getString()
    val databasePassword = environment.config.property("database.password").getString()

    DatabaseSingleton.init(databaseURL, databaseUser, databasePassword)

    configureHTTP()
    configureAuthentication()
    configureRouting()
    configureSerialization()

    val serviceAccountStream = this::class.java.classLoader.getResourceAsStream("service_account_key.json")
    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
        .build()

    FirebaseApp.initializeApp(options)

    launch {
        while (true) {
            // checks for upcoming events every minute and sends notifications if needed
            sendUpcomingEventNotifications()
            delay(30000)
        }
    }
}
