package com.uwconnect.dao

import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.*

object DatabaseSingleton {
    fun init(databaseURL: String, databaseUser: String, databasePassword: String) {
        val driverClassName = "org.postgresql.Driver"
        val database = Database.connect(databaseURL, driverClassName, databaseUser, databasePassword, databaseConfig = DatabaseConfig {
            keepLoadedReferencesOutOfTransaction = true
        })
        transaction(database) {
            SchemaUtils.create(Users, Clubs, Events, Announcements, ClubUsers, EventUsers, Notifications)
            SchemaUtils.createMissingTablesAndColumns(Users, Clubs, Events, Announcements, ClubUsers, EventUsers, Notifications)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}