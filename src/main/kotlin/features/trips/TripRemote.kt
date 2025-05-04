package com.example.features.trips

import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class TripResponse(
    val tripId: Long?,
    val driverUuid: String,
    val driverFirstName: String,
    val driverPicture: ByteArray?,
    val driverLastName: String,
    val startLocationCity: String,
    val startLocationRegion: String,
    val endLocationCity: String,
    val endLocationRegion: String,
    val startTime: String,
    val endTime: String,
    val distanceInMeters: Int,
    val availableSeats: Int,
    val status: String,
    val price: Int,
    val isFastConfirm: Boolean,
    val avgRating: Double?,
    val avgDrivingSkills: Double?
)

@Serializable
data class TripDetailedResponse(
    val tripId: Long?,
    val driverUuid: String,
    val driverFirstName: String,
    val driverLastName: String,
    val driverPicture: ByteArray?,
    val driverPhoneNumber: String?,
    val description: String?,
    val distanceInMeters: Int,
    val availableSeats: Int,
    val status: String,
    val price: Int,
    val isFastConfirm: Boolean,
    val carMake: String,
    val carModel: String,
    val startCity: String,
    val startRegion: String,
    val startLat: String,
    val startLng: String,
    val endCity: String,
    val endRegion: String,
    val endLat: String,
    val endLng: String,
    val startTime: String,
    val endTime: String,
    val avgRating: Double?,
    val avgDrivingSkills: Double?,
    val countReviews: Int
)

@Serializable
data class CreateTripRequest(
    val driverUuid: String,
    val startLocationId: Long,
    val endLocationId: Long,
    val startTime: String,
    val endTime: String,
    val distanceInMeters: Int,
    val availableSeats: Int,
    val price: Int,
    val isFastConfirm: Boolean,
)


@Serializable
data class UpdateTripRequest(
    val status: String? = null,
    val availableSeats: Int? = null
)

