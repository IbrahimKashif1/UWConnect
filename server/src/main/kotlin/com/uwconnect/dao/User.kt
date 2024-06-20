package com.uwconnect.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Users : IntIdTable() {
    val name = varchar("name", 64)
    val email = varchar("email", 128).uniqueIndex()
    val bio = varchar("bio", 256).nullable()
    val passwordHash = varchar("password_hash", 128)
    val deviceToken = varchar("device_token", 256).nullable()
}

class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)
    var name     by Users.name
    var email by Users.email
    var bio by Users.bio
    var passwordHash by Users.passwordHash
    var deviceToken by Users.deviceToken

    var clubs by Club via ClubUsers
    var events by Event via EventUsers
}