package com.example.database.auth

import com.example.database.users.Users
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object Auth : Table("auth") {
    val authUuid = varchar("auth_uuid", 50)
    val userUuid = varchar("user_uuid", 50)
        .references(Users.userUuid, onDelete = ReferenceOption.CASCADE)
    val email = varchar("email", 35)
    val password = varchar("password", 100)
    val isBlocked = bool("is_blocked").default(false)
    val createdAt = timestamp("created_at")

    override val primaryKey = PrimaryKey(authUuid)
}