package com.example.features.login.presentation

import com.example.features.login.domain.LoginService
import com.example.features.auth.LoginRequest
import com.example.utils.validator.LoginValidator
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class LoginController(private val call: ApplicationCall) {
    suspend fun loginUser() {
        val request = call.receive<LoginRequest>()
        val errors = LoginValidator.validateLogin(request.email, request.password)
        if (errors.isNotEmpty()) {
            call.respond(HttpStatusCode.BadRequest, errors)
            return
        }
        val response = LoginService.login(request.email, request.password)
        if (response.success) {
            call.respond(HttpStatusCode.OK, response)
        } else {
            call.respond(HttpStatusCode.Unauthorized, response)
        }
    }
}