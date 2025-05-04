package com.example.features.registration.domain

import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.database.auth.Auth
import com.example.features.registration.repository.RegistrationRepositoryImpl
import com.example.features.trip_requests.Response
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

object RegistrationService {
    @OptIn(ExperimentalUuidApi::class)
    fun registerUser(
        firstName: String,
        lastName: String,
        dateOfBirth: String,
        email: String,
        password: String
    ): Response {
        return transaction {
            val userRow = Auth.select { Auth.email eq email }.singleOrNull()
            if (userRow != null) {
                return@transaction Response(
                    isSuccess = false,
                    message = "Користувач з такою поштою вже існує!"
                )
            }

            val userUuid = Uuid.random().toString()
            val authUuid = Uuid.random().toString()
            val bcryptPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray())
            RegistrationRepositoryImpl.createNewAccount(
                authUuid = authUuid,
                userUuid = userUuid,
                email = email,
                password = bcryptPassword,
                firstName = firstName,
                lastName = lastName,
                dateOfBirth = dateOfBirth
            )
            return@transaction Response(
                isSuccess = true,
                message = "Реєстрація успішна",
            )


        }
    }
}