package com.example.features.users.presentation

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureUserInfoRouting() {
    routing {
        get("/me") {
            UserController(call).getCurrentUser()
        }

        get("/users/{userUuid}") {
            UserController(call).getUser()
        }

        put("/users/{userUuid}") {
            UserController(call).updateUser()
        }

        post("user/{userUuid}") {
            UserController(call).deleteUser()
        }
    }
}