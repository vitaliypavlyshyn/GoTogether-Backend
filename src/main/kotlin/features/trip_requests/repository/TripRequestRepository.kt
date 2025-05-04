package com.example.features.trip_requests.repository

import com.example.database.trip_requests.TripRequestDTO
import com.example.database.trip_requests.TripRequests
import com.example.features.trip_requests.CreateTripRequestRequest
import com.example.features.trip_requests.UpdateTripRequestRequest
import com.example.features.trips.CreateTripRequest

interface TripRequestRepository {
    fun findAllByUserUuid(userUuid: String): List<TripRequestDTO>
    fun findAllByTripId(tripId: Long): List<TripRequestDTO>
    fun findRequestByTripIdAndUSerUuid(tripId: Long, userUuid: String): TripRequestDTO?
    fun findRequestByRequestId(requestId: Long): TripRequestDTO?
    fun saveRequest(request: CreateTripRequestRequest): Boolean
    fun deleteRequest(requestId: Long): Boolean
    fun updateRequest(requestId: Long, request: UpdateTripRequestRequest): Boolean
}