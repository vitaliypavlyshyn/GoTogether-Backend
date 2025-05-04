package com.example.database.trips

import kotlinx.datetime.toJavaInstant
import org.jetbrains.exposed.sql.ResultRow

fun mapToTripDTO(row: ResultRow): TripDTO {
    return TripDTO(
        tripId = row[Trips.tripId],
        driverUuid = row[Trips.driverUuid],
        startLocationId = row[Trips.startLocationId],
        endLocationId = row[Trips.endLocationId],
        startTime = row[Trips.startTime].toJavaInstant(),
        endTime = row[Trips.endTime].toJavaInstant(),
        distanceInMeters = row[Trips.distanceInMeters],
        availableSeats = row[Trips.availableSeats],
        status = row[Trips.status],
        price = row[Trips.price],
        isFastConfirm = row[Trips.isFastConfirm],
        createdAt = row[Trips.createdAt].toJavaInstant(),
        updatedAt = row[Trips.updatedAt].toJavaInstant()
    )
}