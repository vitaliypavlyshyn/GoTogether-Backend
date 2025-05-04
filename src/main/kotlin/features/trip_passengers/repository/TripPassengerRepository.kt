package com.example.features.trip_passengers.repository

import com.example.database.trip_passengers.TripPassengerDTO
import com.example.database.trip_passengers.TripPassengers
import com.example.features.trip_passengers.CreateTripPassengerRequest
import com.example.features.trips.CreateTripRequest

interface TripPassengerRepository {
    fun findPassengersByTripId(tripId: Long): List<TripPassengerDTO>
    fun findPassengersByPassengerUuid(passengerUuid: String): List<TripPassengerDTO>
    fun findPassengerByTripIdAndPassengerUuid(tripId: Long, passengerUuid: String): TripPassengerDTO?
    fun savePassenger(tripPassengerRequest: CreateTripPassengerRequest): Boolean
    fun deletePassenger(tripId: Long, passengerUuid: String): Boolean
}