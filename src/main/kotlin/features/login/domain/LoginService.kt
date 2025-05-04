package com.example.features.login.domain

import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.database.auth.Auth
import com.example.features.auth.LoginResponse
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object LoginService {
    fun login(email: String, password: String): LoginResponse {
        return transaction {
            val userRow = Auth.select { Auth.email eq email }.singleOrNull()

            if (userRow == null) {
                return@transaction LoginResponse(
                    success = false,
                    message = "Користувача з такою поштою не знайдено."
                )
            }

            val isBlocked = userRow[Auth.isBlocked]
            val hashedPassword = userRow[Auth.password]
            val passwordMatches = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified

            if(isBlocked) {
                return@transaction LoginResponse(
                    success = false,
                    message = "Обліковий запис заблоковано"
                )
            }
            if (!passwordMatches) {
                return@transaction LoginResponse(
                    success = false,
                    message = "Невірний пароль."
                )
            }

            val token = userRow[Auth.userUuid]

            return@transaction LoginResponse(
                success = true,
                message = "Вхід успішний",
                token = token
            )
        }
    }
}
