package com.example.features.registration.presentation

import com.example.features.registration.RegistrationRequest
import com.example.features.registration.domain.RegistrationService
import com.example.utils.validator.LoginValidator
import com.example.utils.validator.UserValidator
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toLocalDate

class RegistrationController (private val call: ApplicationCall) {
    suspend fun registerUser() {
        val request = call.receive<RegistrationRequest>()
        val authErrors = LoginValidator.validateLogin(request.email, request.password)
        val userErrors = UserValidator.validateUserInput(
            firstName = request.firstName,
            lastName = request.lastName,
            dateOfBirth = request.dateOfBirth.toLocalDate().toJavaLocalDate(),
            phoneNumber = null,
            description = null
        )
        if (authErrors.isNotEmpty()) {
            call.respond(HttpStatusCode.BadRequest, authErrors)
            return
        }
        if (userErrors.isNotEmpty()) {
            call.respond(HttpStatusCode.BadRequest, userErrors)
            return
        }
        val response = RegistrationService.registerUser(
            firstName = request.firstName,
            lastName = request.lastName,
            dateOfBirth = request.dateOfBirth,
            email = request.email,
            password = request.password
        )

        call.respond(HttpStatusCode.OK, response)

    }
}