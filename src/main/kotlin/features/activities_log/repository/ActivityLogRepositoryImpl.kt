package com.example.features.activities_log.repository

import com.example.database.activity_logs.ActivitiesLog
import com.example.database.activity_logs.ActivityLogDTO
import com.example.database.activity_logs.mapToActivityLogDTO
import com.example.database.users.Users
import com.example.features.activities_log.ActivityLogRequest
import kotlinx.datetime.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

object ActivityLogRepositoryImpl : ActivityLogRepository {
    override fun findActivitiesLogByUserUuid(userUuid: String): List<ActivityLogDTO> {
        return transaction {
            ActivitiesLog.select { ActivitiesLog.userUuid eq userUuid }.map {
                mapToActivityLogDTO(it)
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun saveActivity(activityRequest: ActivityLogRequest): Boolean {
        return try {
            transaction {
                val result  = ActivitiesLog.insert {
                    it[logUuid] = Uuid.random().toString()
                    it[userUuid] = activityRequest.userUuid
                    it[device] = activityRequest.device
                    it[os] = activityRequest.os
                    it[publicIp] = activityRequest.publicIp
                    it[entryDate] = Instant.parse(activityRequest.entryDate).toKotlinInstant()
                }
                 result.insertedCount > 0 || result.resultedValues?.isNotEmpty() == true
            }
        } catch (e: Exception) {
            false
        }
    }
}