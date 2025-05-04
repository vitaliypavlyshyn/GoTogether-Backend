package com.example.features.cars.presentation

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureCarRouting() {
    routing {
        get("cars") {
            CarController(call).getCars()
        }

        get("cars/{carId}") {
            CarController(call).getCarById()
        }
    }
}