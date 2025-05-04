package com.example.database.trip_requests

import com.example.database.cars.toCarResponse
import com.example.database.cars.toCarsResponse
import com.example.database.users.UserDTO
import com.example.features.cars.CarResponse
import com.example.features.trip_requests.TripRequestResponse
import java.time.Instant

data class TripRequestDTO(
    val requestId: Long,
    val tripId: Long,
    val passengerUuid: String,
    val requestedSeats: Int,
    val status: String,
    val createdAt: Instant,
    val updatedAt: Instant
)

fun TripRequestDTO.toTripRequestResponse(): TripRequestResponse {
    return TripRequestResponse(
        requestId = this.requestId,
        tripId = this.tripId,
        passengerUuid = this.passengerUuid,
        requestedSeats = this.requestedSeats,
        status = this.status,
    )
}

fun TripRequestDTO.toTripRequestResponse(userDTO: UserDTO): TripRequestResponse {
    return TripRequestResponse(
        requestId = this.requestId,
        tripId = this.tripId,
        passengerUuid = this.passengerUuid,
        passengerFirstName = userDTO.firstName,
        passengerLastName = userDTO.lastName,
        requestedSeats = this.requestedSeats,
        status = this.status,
        pictureProfile = userDTO.pictureProfile,
        phoneNumber = userDTO.phoneNumber
    )
}

fun List<TripRequestDTO>.toTripRequestsResponse(): List<TripRequestResponse> {
    val tripRequestsResponse = mutableListOf<TripRequestResponse>()
    for(i in this) {
        tripRequestsResponse.add(i.toTripRequestResponse())
    }
    return tripRequestsResponse

}