package com.example.features.activities_log.presentation

import com.example.database.reviews.toRatingResponse
import com.example.features.activities_log.ActivityLogRequest
import com.example.features.activities_log.domain.ActivityLogService
import com.example.utils.Controller
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class ActivityLogController(private val call: ApplicationCall): Controller() {
    suspend fun getActivitiesLogByAuthUuid() {
        val user = checkToken(call)

        if(user == null) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid or expired token")
        } else {
            val userUuid = call.parameters["userUuid"]

            if (userUuid == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid auth UUID")
                return
            }

            val activitiesLog = ActivityLogService.fetchActivitiesLogByUserUuid(userUuid)
            if(activitiesLog != null) {
                call.respond(HttpStatusCode.OK, activitiesLog)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Account not exist")
            }
        }
    }

    suspend fun postActivity() {
        val request = call.receive<ActivityLogRequest>()
        val user = checkToken(call)

        if (user == null) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid or expired token")
        } else {
            if (user.userUuid == request.userUuid) {
                val response = ActivityLogService.saveActivity(request)
                if (response != null) {
                    call.respond(HttpStatusCode.OK, response)
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "Помилка відповіді")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Different Users")
            }
        }
    }
}