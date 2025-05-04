package com.example.features.registration

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationRequest(
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String,
    val email: String,
    val password: String
)
