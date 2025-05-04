package com.example.features.locations.presentation

import com.example.features.cars.presentation.CarController
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureLocationRouting() {
    routing {
        get("locations") {
            LocationController(call).getLocations()
        }

        get("locations/{id}") {
            LocationController(call).getLocationById()
        }
    }
}