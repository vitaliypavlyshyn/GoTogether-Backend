package com.example.features.login.presentation

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureLoginRouting() {
    routing {
        post("/login") {
            LoginController(call).loginUser()
        }
    }
}