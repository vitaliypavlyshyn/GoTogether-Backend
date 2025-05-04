package com.example.features.activities_log.domain

import com.example.database.activity_logs.toActivitiesLogResponse
import com.example.database.users.Users
import com.example.features.activities_log.ActivityLogRequest
import com.example.features.activities_log.ActivityLogResponse
import com.example.features.activities_log.repository.ActivityLogRepositoryImpl
import com.example.features.trip_requests.Response
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object ActivityLogService {
    fun fetchActivitiesLogByUserUuid(userUuid: String): List<ActivityLogResponse>? {
        transaction {
            Users.select { Users.userUuid eq userUuid }
                .singleOrNull()
                ?.get(Users.userUuid)
        } ?: return null

        return ActivityLogRepositoryImpl.findActivitiesLogByUserUuid(userUuid).toActivitiesLogResponse()
    }

    fun saveActivity(activityRequest: ActivityLogRequest): Response {
        if(ActivityLogRepositoryImpl.saveActivity(activityRequest)) {
            return Response(
                isSuccess = true,
                message = "Діяльність було записано"
            )
        }
        return Response(
            isSuccess = false,
            message = "Помилка запису діяльності"
        )
    }
}