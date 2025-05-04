package com.example.features.trip_requests

import kotlinx.serialization.Serializable

@Serializable
data class TripRequestResponse(
    val requestId: Long,
    val tripId: Long,
    val passengerUuid: String,
    val passengerFirstName: String? = null,
    val passengerLastName: String? = null,
    val pictureProfile: ByteArray? = null,
    val phoneNumber: String? = null,
    val requestedSeats: Int,
    val status: String,
)

@Serializable
data class CreateTripRequestRequest(
    val tripId: Long,
    val passengerUuid: String,
    val requestedSeats: Int
)


@Serializable
data class UpdateTripRequestRequest(
    val status: String? = null,
)

@Serializable
data class Response(
    val message: String,
    val isSuccess: Boolean,
)