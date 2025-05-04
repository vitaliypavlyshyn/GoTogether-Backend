package com.example.database.auth

import com.example.features.auth.LoginRequest
import kotlinx.serialization.Serializable
import java.time.Instant

//@Serializable
data class AuthDTO(
    val authUuid: String,
    val userUuid: String,
    val email: String,
    val password: String,
    val isBlocked: Boolean = false,
    val createdAt: Instant
)

fun AuthDTO.toLoginRequest(): LoginRequest {
    return LoginRequest(
    email = email,
    password = password
    )
}