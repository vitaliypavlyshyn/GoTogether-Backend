package com.example.features.trips.presentation

import com.example.features.users.presentation.UserController
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureTripRouting() {
    routing {
        get("/trips") {
            TripController(call).getAllTripsByDate()
        }
        get("/trips/driver/{driverUuid}") {
            TripController(call).getAllTripsByDriverUuid()
        }
        get("/trips/passenger/{passengerUuid}") {
            TripController(call).getAllTripsByPassengerUuid()
        }
        get("/trips/requester/{requesterUuid}") {
            TripController(call).getAllTripsByRequesterUuid()
        }
        get("/trips/{tripId}") {
            TripController(call).getTripByTripId()
        }
        post("/trips") {
            TripController(call).postTrip()
        }
        put("/trips/{tripId}") {
            TripController(call).updateTrip()
        }
        delete("/trips/{tripId}") {
            TripController(call).deleteTrip()
        }
    }
}