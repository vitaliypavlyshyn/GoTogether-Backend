package com.example.features.trip_requests.presentation

import com.example.features.trips.presentation.TripController
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureTripRequestRouting() {
    routing {
        get("/requests/{tripId}") {
            TripRequestController(call).getRequestsByTripId()
        }
        get("/requests/user/{userUuid}") {
            TripRequestController(call).getRequestsByUserUuid()
        }
        get("/request/{tripId}/{userUuid}") {
            TripRequestController(call).getRequestByTripIdAndUserUuid()
        }
        post("/request") {
            TripRequestController(call).postTripRequest()
        }
        delete("/request/{requestId}") {
            TripRequestController(call).deleteTripRequest()
        }
        put("/request/{requestId}") {
            TripRequestController(call).updateRequest()
        }
    }
}