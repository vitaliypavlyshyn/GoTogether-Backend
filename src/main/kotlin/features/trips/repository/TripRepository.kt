package com.example.features.trips.repository

import com.example.database.trips.TripDTO
import com.example.features.trips.CreateTripRequest
import com.example.features.trips.UpdateTripRequest
import java.time.LocalDate

interface TripRepository {
    fun findTripsByParameters(fromCityId: Long, toCityId: Long, date: String): List<TripDTO>
    fun findTripsByDriverUuid(driverUuid: String): List<TripDTO>
    fun findByTripId(id: Long): TripDTO?
    fun findAll(): List<TripDTO>
    fun saveTrip(request: CreateTripRequest): Boolean
    fun updateTrip(tripId: Long, request: UpdateTripRequest): Boolean
    fun deleteTrip(tripId: Long): Boolean
}