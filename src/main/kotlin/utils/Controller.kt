package com.example.utils

import com.example.features.users.UserResponse
import com.example.features.users.domain.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

open class Controller {
    protected suspend fun checkToken(call: ApplicationCall): UserResponse? {
        val token = call.request.headers["Authorization"]?.removePrefix("Bearer ")?.trim()

        if (token == null) {
            call.respond(HttpStatusCode.Unauthorized, "Token not provided")
            return null
        }

        return UserService.fetchUserByToken(token)
    }
}