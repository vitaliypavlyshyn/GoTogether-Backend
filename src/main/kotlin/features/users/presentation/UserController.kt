package com.example.features.users.presentation

import com.example.features.users.UpdateUserRequest
import com.example.features.users.domain.UserService
import com.example.utils.Controller
import com.example.utils.extensions.capitalizeName
import com.example.utils.parser.ParserDate
import com.example.utils.validator.UserValidator
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class UserController(private val call: ApplicationCall): Controller() {
    suspend fun getCurrentUser() {
        val user = checkToken(call)

        if (user == null) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid or expired token")
        } else {
            call.respond(HttpStatusCode.OK, user)
        }
    }

    suspend fun getUser() {
        val user = checkToken(call)

        if (user == null) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid or expired token")
        } else {
            val userUuid = call.parameters["userUuid"]

            if (userUuid == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid user UUID")
                return
            }

            val concreteUser = UserService.fetchUserByUuid(userUuid)
            if (concreteUser == null) {
                call.respond(HttpStatusCode.NotFound, "User not found")
            } else {
                call.respond(HttpStatusCode.OK, concreteUser)
            }
        }
    }

    suspend fun updateUser() {
        val user = checkToken(call)

        if (user == null) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid or expired token")
            return
        }

        val userUuid = call.parameters["userUuid"]
        if (userUuid == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid user UUID")
            return
        }

        val request = call.receive<UpdateUserRequest>()

        val errors = UserValidator.validateUserInput(
            phoneNumber = request.phoneNumber,
            description = request.description,
            firstName = request.firstName?.capitalizeName(),
            lastName = request.lastName?.capitalizeName(),
            dateOfBirth = ParserDate.safeParseDate(request.dateOfBirth)
        )
        if (errors.isNotEmpty()) {
            call.respond(HttpStatusCode.BadRequest, errors)
            return
        }
        val response = UserService.updateUser(userUuid, request)
        call.respond(HttpStatusCode.OK, response)
    }


    suspend fun deleteUser() {
     //   val request = call.receive<DeleteUserRequest>()
        val user = checkToken(call)
        val userUuid = call.parameters["userUuid"]
        if (userUuid == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid user UUID")
            return
        }
        if (user == null) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid or expired token")
        } else {
            if (user.userUuid == userUuid) {
                val response = UserService.deleteUser(userUuid)
                call.respond(HttpStatusCode.OK, response)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Different Users")
            }
        }
    }
}
