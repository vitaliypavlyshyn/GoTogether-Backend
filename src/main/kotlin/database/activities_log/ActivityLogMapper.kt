package com.example.database.activity_logs

import kotlinx.datetime.toJavaInstant
import org.jetbrains.exposed.sql.ResultRow

fun mapToActivityLogDTO(row: ResultRow): ActivityLogDTO {
    return ActivityLogDTO(
        logUuid = row[ActivitiesLog.logUuid],
        userUuid = row[ActivitiesLog.userUuid],
        device = row[ActivitiesLog.device],
        os = row[ActivitiesLog.os],
        publicIp = row[ActivitiesLog.publicIp],
        entryDate = row[ActivitiesLog.entryDate].toJavaInstant()
    )
}