package com.example.features.locations

import kotlinx.serialization.Serializable

@Serializable
data class LocationResponse(
    val cityId: Long,
    val regionId: Long,
    val cityName: String,
    val cityNameUk: String,
    val lat: String,
    val lng: String,
    val country: String,
    val iso2: String,
    val adminName: String,
    val adminNameUk: String
)