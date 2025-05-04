package com.example.features.cars

import kotlinx.serialization.Serializable

@Serializable
data class CarResponse(
    val carId: Long,
    val make: String,
    val model: String
)