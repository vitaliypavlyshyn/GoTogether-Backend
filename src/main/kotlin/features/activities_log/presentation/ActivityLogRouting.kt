package com.example.features.activities_log.presentation

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureActivityLogRouting() {
    routing {
        get("/activity/{userUuid}") {
            ActivityLogController(call).getActivitiesLogByAuthUuid()
        }
        post("/activity") {
            ActivityLogController(call).postActivity()
        }
    }
}