package com.example.features.registration.presentation

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRegisterRouting() {
    routing {
        post("/register") {
            RegistrationController(call).registerUser()
        }
    }
}