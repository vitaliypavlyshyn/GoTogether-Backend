package com.example.features.activities_log.repository

import com.example.database.activity_logs.ActivityLogDTO
import com.example.features.activities_log.ActivityLogRequest

interface ActivityLogRepository {
    fun findActivitiesLogByUserUuid(userUuid: String): List<ActivityLogDTO>
    fun saveActivity(activityRequest: ActivityLogRequest): Boolean
}