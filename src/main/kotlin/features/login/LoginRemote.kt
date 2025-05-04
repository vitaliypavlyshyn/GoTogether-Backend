package com.example.features.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null
)