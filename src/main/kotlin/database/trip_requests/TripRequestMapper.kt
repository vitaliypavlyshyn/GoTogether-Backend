package com.example.database.trip_requests

import com.example.database.regions.RegionDTO
import com.example.database.regions.Regions
import kotlinx.datetime.toJavaInstant
import org.jetbrains.exposed.sql.ResultRow

fun mapToTripRequestDTO(row: ResultRow): TripRequestDTO {
    return TripRequestDTO(
        requestId = row[TripRequests.requestId],
        tripId = row[TripRequests.tripId],
        passengerUuid = row[TripRequests.passengerUuid],
        requestedSeats = row[TripRequests.requestedSeats],
        status = row[TripRequests.status],
        createdAt = row[TripRequests.createdAt].toJavaInstant(),
        updatedAt = row[TripRequests.updatedAt].toJavaInstant()
    )
}