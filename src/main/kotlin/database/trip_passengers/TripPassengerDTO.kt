package com.example.database.trip_passengers

import com.example.features.trip_passengers.TripPassengerResponse
import com.example.features.users.UserResponse
import java.time.Instant

data class TripPassengerDTO(
    val tripPassengerId: Long,
    val tripId: Long,
    val passengerUuid: String,
    val seatsBooked: Int,
    val createdAt: Instant
)

fun TripPassengerDTO.toTripPassengerResponse(userResponse: UserResponse): TripPassengerResponse {
    return TripPassengerResponse(
        tripPassengerId = this.tripPassengerId,
        tripId = this.tripId,
        passengerUuid = userResponse.userUuid,
        firstName = userResponse.firstName,
        lastName = userResponse.lastName,
        pictureProfile = userResponse.pictureProfile,
        seatsBooked = this.seatsBooked,
        avgRating = userResponse.avgRating,
        countReviews = userResponse.countReviews,
        phoneNumber = userResponse.phoneNumber
    )
}