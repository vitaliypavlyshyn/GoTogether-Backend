package com.example.database.trips

import com.example.database.cars.CarDTO
import com.example.features.cars.CarResponse
import com.example.features.locations.LocationResponse
import com.example.features.trips.TripDetailedResponse
import com.example.features.trips.TripResponse
import com.example.features.users.UserResponse
import java.time.Instant

data class TripDTO(
    val tripId: Long,
    val driverUuid: String,
    val startLocationId: Long,
    val endLocationId: Long,
    val startTime: Instant,
    val endTime: Instant,
    val distanceInMeters: Int,
    val availableSeats: Int,
    val status: String,
    val price: Int,
    val isFastConfirm: Boolean,
    val createdAt: Instant,
    val updatedAt: Instant
)

fun TripDTO.toTripResponse(
    userResponse: UserResponse,
    startLocationResponse: LocationResponse,
    endLocationResponse: LocationResponse
): TripResponse {
    return TripResponse(
        tripId = this.tripId,
        driverUuid = this.driverUuid,
        driverFirstName = userResponse.firstName,
        driverLastName = userResponse.lastName,
        driverPicture = userResponse.pictureProfile,
        startLocationCity = startLocationResponse.cityNameUk,
        startLocationRegion = startLocationResponse.adminNameUk,
        endLocationCity = endLocationResponse.cityNameUk,
        endLocationRegion = endLocationResponse.adminNameUk,
        startTime = this.startTime.toString(),
        endTime = this.endTime.toString(),
        distanceInMeters = this.distanceInMeters,
        availableSeats = this.availableSeats,
        status = this.status,
        price = this.price,
        isFastConfirm = this.isFastConfirm,
        avgRating = userResponse.avgRating,
        avgDrivingSkills = userResponse.avgDrivingSkills
    )
}

fun TripDTO.toTripDetailedResponse(
    driver: UserResponse,
    startLocation: LocationResponse,
    endLocation: LocationResponse,
    car: CarDTO
): TripDetailedResponse {
    return TripDetailedResponse(
        tripId = this.tripId,
        driverUuid = this.driverUuid,
        driverFirstName = driver.firstName,
        driverLastName = driver.lastName,
        driverPicture = driver.pictureProfile,
        driverPhoneNumber = driver.phoneNumber,
        distanceInMeters = this.distanceInMeters,
        availableSeats = this.availableSeats,
        status = this.status,
        price = this.price,
        isFastConfirm = this.isFastConfirm,
        description = driver.description,
        startCity = startLocation.cityNameUk,
        startRegion = startLocation.adminNameUk,
        startLat = startLocation.lat,
        startLng = startLocation.lng,
        endCity = endLocation.cityNameUk,
        endRegion = endLocation.adminNameUk,
        endLat = endLocation.lat,
        endLng = endLocation.lng,
        startTime = this.startTime.toString(),
        endTime = this.endTime.toString(),
        carMake = car.make,
        carModel = car.model,
        avgRating = driver.avgRating,
        avgDrivingSkills = driver.avgDrivingSkills,
        countReviews = driver.countReviews,
    )
}