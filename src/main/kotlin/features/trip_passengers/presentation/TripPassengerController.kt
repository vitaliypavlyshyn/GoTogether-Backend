package com.example.features.trip_passengers.presentation

import com.example.features.trip_passengers.CreateTripPassengerRequest
import com.example.features.trip_passengers.domain.TripPassengerService
import com.example.utils.Controller
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class TripPassengerController(private val call: ApplicationCall): Controller() {
    suspend fun getPassengersByTripId() {
        val user = checkToken(call)

        if (user == null) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid or expired token")
        } else {
            val idParam = call.parameters["tripId"]
            val tripId = idParam?.toLongOrNull()

            if (tripId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid trip ID")
                return
            }
            val tripPassengers = TripPassengerService.fetchPassengersByTripId(tripId)
            call.respond(HttpStatusCode.OK, tripPassengers)
        }
    }

    suspend fun postTripPassenger() {
        val request = call.receive<CreateTripPassengerRequest>()
        val user = checkToken(call)

        if (user == null) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid or expired token")
        } else {
            if (user.userUuid == request.passengerUuid) {
                val response = TripPassengerService.createPassenger(request)
                if (response.isSuccess) {
                    call.respond(HttpStatusCode.OK, response)
                } else {
                    call.respond(HttpStatusCode.OK, response)
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Different Users")
            }
        }
    }

    suspend fun deletePassenger() {
        val user = checkToken(call)

        if (user == null) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid or expired token")
        } else {
            val tripId = call.parameters["tripId"]?.toLongOrNull()
            val passengerUuid = call.parameters["passengerUuid"]


            if (tripId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid trip UUID")
                return
            }
            if (passengerUuid == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid passenger UUID")
                return
            }

            if (user.userUuid == passengerUuid) {
                val response = TripPassengerService.deletePassenger(tripId, passengerUuid)
                if (response.isSuccess) {
                    call.respond(HttpStatusCode.OK, response)
                } else {
                    call.respond(HttpStatusCode.OK, response)
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Different Users")
            }
        }
    }
}