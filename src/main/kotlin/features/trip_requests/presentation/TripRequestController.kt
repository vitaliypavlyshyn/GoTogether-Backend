package com.example.features.trip_requests.presentation

import com.example.features.trip_passengers.CreateTripPassengerRequest
import com.example.features.trip_passengers.domain.TripPassengerService
import com.example.features.trip_requests.CreateTripRequestRequest
import com.example.features.trip_requests.UpdateTripRequestRequest
import com.example.features.trip_requests.domain.TripRequestService
import com.example.features.trip_requests.repository.TripRequestRepositoryImpl
import com.example.features.trips.UpdateTripRequest
import com.example.features.trips.domain.TripService
import com.example.utils.Controller
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class TripRequestController(private val call: ApplicationCall) : Controller() {
    suspend fun getRequestsByTripId() {
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
            val trip = TripRequestService.fetchRequestsByTripId(tripId)
            call.respond(HttpStatusCode.OK, trip)
        }
    }

    suspend fun getRequestsByUserUuid() {
        val user = checkToken(call)

        if (user == null) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid or expired token")
        } else {
            val userUuid = call.parameters["userUuid"]

            if (userUuid == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid user UUID")
                return
            }
            val requests = TripRequestService.fetchRequestsByUserUuid(userUuid)
            if(requests != null) {
                call.respond(HttpStatusCode.OK, requests)
            } else {
                call.respond(HttpStatusCode.BadRequest, "User not exist")
            }
        }
    }

    suspend fun getRequestByTripIdAndUserUuid() {
        val user = checkToken(call)

        if (user == null) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid or expired token")
        } else {
            val tripId = call.parameters["tripId"]?.toLongOrNull()
            val userUuid = call.parameters["userUuid"]


            if (tripId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid trip UUID")
                return
            }
            if (userUuid == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid user UUID")
                return
            }
            val requests = TripRequestService.fetchRequestByTripIdAndUserUuid(tripId, userUuid)
            if(requests != null) {
                call.respond(HttpStatusCode.OK, requests)
            } else {
                call.respond(HttpStatusCode.BadRequest, "User not exist")
            }
        }
    }

    suspend fun updateRequest() {
        val user = checkToken(call)

        if (user == null) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid or expired token")
            return
        }
        val idParam = call.parameters["requestId"]
        val requestId = idParam?.toLongOrNull()

        if (requestId == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid request ID")
            return
        }

        val request = call.receive<UpdateTripRequestRequest>()

        val response = TripRequestService.updateRequest(requestId, request)
        call.respond(HttpStatusCode.OK, response)

    }

    suspend fun postTripRequest() {
        val request = call.receive<CreateTripRequestRequest>()
        val user = checkToken(call)

        if (user == null) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid or expired token")
        } else {
            if (user.userUuid == request.passengerUuid) {
                val response = TripRequestService.createRequest(request)
                call.respond(HttpStatusCode.OK, response)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Different Users")
            }
        }
    }

    suspend fun deleteTripRequest() {
        val user = checkToken(call)

        if (user == null) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid or expired token")
        } else {
            val idParam = call.parameters["requestId"]
            val requestId = idParam?.toLongOrNull()

            if (requestId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid request ID")
                return
            }

            val myTripRequest = TripRequestRepositoryImpl.findRequestByRequestId(requestId)
            if(myTripRequest == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid trip request")
                return
            }

            if (user.userUuid == myTripRequest.passengerUuid) {
                val response = TripRequestService.deleteRequest(requestId)
                call.respond(HttpStatusCode.OK, response)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Different Users")
            }
        }
    }
}