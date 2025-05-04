package com.example.database.activity_logs

import com.example.database.cars.toCarResponse
import com.example.database.cars.toCarsResponse
import com.example.features.activities_log.ActivityLogResponse
import com.example.features.cars.CarResponse
import java.time.Instant

data class ActivityLogDTO(
    val logUuid: String,
    val userUuid: String,
    val device: String,
    val os: String,
    val publicIp: String,
    val entryDate: Instant
)

fun ActivityLogDTO.toActivityLogResponse(): ActivityLogResponse {
    return ActivityLogResponse(
        userUuid = this.userUuid,
        device = this.device,
        os = this.os,
        publicIp = this.publicIp,
        entryDate = this.entryDate.toString()
    )
}

fun List<ActivityLogDTO>.toActivitiesLogResponse(): List<ActivityLogResponse> {
    val activitiesLogResponse = mutableListOf<ActivityLogResponse>()
    for(i in this) {
        activitiesLogResponse.add(i.toActivityLogResponse())
    }
    return activitiesLogResponse
}