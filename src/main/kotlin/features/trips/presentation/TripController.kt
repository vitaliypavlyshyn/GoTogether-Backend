package com.example.features.trips.presentation

import com.example.features.activities_log.ActivityLogRequest
import com.example.features.activities_log.domain.ActivityLogService
import com.example.features.trip_requests.domain.TripRequestService
import com.example.features.trip_requests.repository.TripRequestRepositoryImpl
import com.example.features.trips.CreateTripRequest
import com.example.features.trips.UpdateTripRequest
import com.example.features.trips.domain.TripService
import com.example.features.trips.repository.TripRepositoryImpl
import com.example.features.users.UpdateUserRequest
import com.example.features.users.domain.UserService
import com.example.utils.Controller
import com.example.utils.extensions.capitalizeName
import com.example.utils.parser.ParserDate
import com.example.utils.validator.UserValidator
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class TripController(private val call: ApplicationCall) : Controller() {
    suspend fun getAllTripsByDate() {
        val user = checkToken(call)

        if (user == null) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid or expired token")
        } else {
            val fromCityId = call.request.queryParameters["fromCityId"]?.toLongOrNull()
            val toCityId = call.request.queryParameters["toCityId"]?.toLongOrNull()
            val date = call.request.queryParameters["date"]

            if (fromCityId == null || toCityId == null || date == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing or invalid query parameters")
                return
            }

            val trips = TripService.fetchTrips(fromCityId, toCityId, date)
            call.respond(HttpStatusCode.OK, trips)
        }
    }

    suspend fun getTripByTripId() {
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

            val trip = TripService.fetchDetailedTripById(tripId)
            if (trip == null) {
                call.respond(HttpStatusCode.NotFound, "Trip not found")
            } else {
                call.respond(HttpStatusCode.OK, trip)
            }
        }
    }

    suspend fun getAllTripsByDriverUuid() {
        val user = checkToken(call)

        if (user == null) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid or expired token")
        } else {
            val driverUuid = call.parameters["driverUuid"]

            if (driverUuid == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing or invalid query parameters")
                return
            }

            val trips = TripService.fetchTripsByDriverUuid(driverUuid)
            call.respond(HttpStatusCode.OK, trips)
        }
    }

    suspend fun getAllTripsByPassengerUuid() {
        val user = checkToken(call)

        if (user == null) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid or expired token")
        } else {
            val passengerUuid = call.parameters["passengerUuid"]

            if (passengerUuid == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing or invalid query parameters")
                return
            }

            val trips = TripService.fetchTripsByPassengerUuid(passengerUuid)
            call.respond(HttpStatusCode.OK, trips)
        }
    }

    suspend fun getAllTripsByRequesterUuid() {
        val user = checkToken(call)

        if (user == null) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid or expired token")
        } else {
            val requesterUuid = call.parameters["requesterUuid"]

            if (requesterUuid == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing or invalid query parameters")
                return
            }

            val trips = TripService.fetchTripsByRequesterUuid(requesterUuid)
            call.respond(HttpStatusCode.OK, trips)
        }
    }

    suspend fun updateTrip() {
        val user = checkToken(call)

        if (user == null) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid or expired token")
            return
        }
        val idParam = call.parameters["tripId"]
        val tripId = idParam?.toLongOrNull()

        if (tripId == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid trip ID")
            return
        }

        val request = call.receive<UpdateTripRequest>()

        val response = TripService.updateTrip(tripId, request)
        call.respond(HttpStatusCode.OK, response)

    }


    suspend fun postTrip() {
        val request = call.receive<CreateTripRequest>()
        val user = checkToken(call)

        if (user == null) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid or expired token")
        } else {
            if (user.userUuid == request.driverUuid) {
                val response = TripService.createTrip(request)
                call.respond(HttpStatusCode.OK, response)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Different Users")
            }
        }
    }

    suspend fun deleteTrip() {
        val user = checkToken(call)

        if (user == null) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid or expired token")
        } else {
            val idParam = call.parameters["tripId"]
            val tripId = idParam?.toLongOrNull()

            if (tripId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid tripId ID")
                return
            }

            val myTrip = TripRepositoryImpl.findByTripId(tripId)
            if (myTrip == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid tripId")
                return
            }

            if (user.userUuid == myTrip.driverUuid) {
                val response = TripService.deleteTrip(tripId)
                call.respond(HttpStatusCode.OK, response)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Different Users")
            }
        }
    }
}

