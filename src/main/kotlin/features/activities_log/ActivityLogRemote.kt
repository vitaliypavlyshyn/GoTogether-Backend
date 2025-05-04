package com.example.features.activities_log

import kotlinx.serialization.Serializable


@Serializable
data class ActivityLogRequest(
    val userUuid: String,
    val device: String,
    val os: String,
    val publicIp: String,
    val entryDate: String
)

@Serializable
data class ActivityLogResponse(
    val userUuid: String,
    val device: String,
    val os: String,
    val publicIp: String,
    val entryDate: String
)