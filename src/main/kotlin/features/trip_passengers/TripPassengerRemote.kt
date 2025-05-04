package com.example.features.trip_passengers

import kotlinx.serialization.Serializable

@Serializable
data class TripPassengerResponse(
    val tripPassengerId: Long,
    val tripId: Long,
    val passengerUuid: String,
    val firstName: String,
    val lastName: String,
    val pictureProfile: ByteArray?,
    val phoneNumber: String?,
    val seatsBooked: Int,
    val avgRating: Double?,
    val countReviews: Int
)


@Serializable
data class CreateTripPassengerRequest(
    val tripId: Long,
    val passengerUuid: String,
    val seatsBooked: Int,
)

