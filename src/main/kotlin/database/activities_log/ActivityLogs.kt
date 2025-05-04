package com.example.database.activity_logs

import com.example.database.auth.Auth
import com.example.database.users.Users
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object ActivitiesLog: Table("activities_log") {
    val logUuid = varchar("log_uuid", 50)
    val userUuid = varchar("user_uuid", 50)
        .references(Users.userUuid)
    val device = varchar("device", 60)
    val os = varchar("os", 20)
    val publicIp = varchar("public_ip", 50)
    val entryDate = timestamp("entry_date")

    override val primaryKey = PrimaryKey(logUuid)
}