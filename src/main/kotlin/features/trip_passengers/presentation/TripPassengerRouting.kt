package com.example.features.trip_passengers.presentation

import com.example.database.trip_passengers.TripPassengers
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureTripPassengerRouting() {
    routing {
        get("/passengers/{tripId}") {
            TripPassengerController(call).getPassengersByTripId()
        }
        post("/passenger") {
            TripPassengerController(call).postTripPassenger()
        }
        post("/passengers/{tripId}/{passengerUuid}") {
            TripPassengerController(call).deletePassenger()
        }
    }
}