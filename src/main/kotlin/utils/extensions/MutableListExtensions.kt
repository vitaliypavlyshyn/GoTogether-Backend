package com.example.utils.extensions

import com.example.database.trip_passengers.TripPassengerDTO
import com.example.database.trip_requests.TripRequestDTO
import com.example.database.trips.TripDTO
import com.example.features.trips.repository.TripRepositoryImpl
import java.time.Instant

fun MutableList<Pair<Instant, Instant>>.toRequestsTimeRange(
    requests: List<TripRequestDTO>
): MutableList<Pair<Instant, Instant>> {
    val requestsTimeRange = mutableListOf<Pair<Instant, Instant>>()

    for(i in requests) {
        val trip = TripRepositoryImpl.findByTripId(i.tripId)
        if(trip !=null) {
            requestsTimeRange.add(trip.startTime to trip.endTime)
        }
    }
    return requestsTimeRange
}

fun MutableList<Pair<Instant, Instant>>.toBookedTripsTimeRange(
    bookedTrips: List<TripPassengerDTO>
): MutableList<Pair<Instant, Instant>> {
    val bookedTripsTimeRange = mutableListOf<Pair<Instant, Instant>>()

    for(i in bookedTrips) {
        val trip = TripRepositoryImpl.findByTripId(i.tripId)
        if(trip !=null) {
            bookedTripsTimeRange.add(trip.startTime to trip.endTime)
        }
    }
    return bookedTripsTimeRange
}

fun MutableList<Pair<Instant, Instant>>.toCreatedTripsTimeRange(
    createdTrips: List<TripDTO>
): MutableList<Pair<Instant, Instant>> {
    val createdTripsTimeRange = mutableListOf<Pair<Instant, Instant>>()

    for(i in createdTrips) {
        val trip = TripRepositoryImpl.findByTripId(i.tripId)
        if(trip !=null) {
            createdTripsTimeRange.add(trip.startTime to trip.endTime)
        }
    }
    return createdTripsTimeRange
}