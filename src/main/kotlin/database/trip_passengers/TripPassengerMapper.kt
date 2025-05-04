package com.example.database.trip_passengers

import kotlinx.datetime.toJavaInstant
import org.jetbrains.exposed.sql.ResultRow

fun mapToTripPassengerDTO(row: ResultRow): TripPassengerDTO {
    return TripPassengerDTO(
        tripPassengerId = row[TripPassengers.tripPassengerId],
        tripId = row[TripPassengers.tripId],
        passengerUuid = row[TripPassengers.passengerUuid],
        seatsBooked = row[TripPassengers.seatsBooked],
        createdAt = row[TripPassengers.createdAt].toJavaInstant()
    )
}