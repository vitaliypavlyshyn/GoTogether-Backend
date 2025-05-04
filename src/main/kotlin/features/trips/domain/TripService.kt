package com.example.features.trips.domain

import com.example.database.cars.CarDTO
import com.example.database.trip_requests.TripRequestStatus
import com.example.database.trips.TripDTO
import com.example.database.trips.toTripDetailedResponse
import com.example.database.trips.toTripResponse
import com.example.features.cars.domain.CarService
import com.example.features.locations.domain.LocationService
import com.example.features.trip_passengers.repository.TripPassengerRepositoryImpl
import com.example.features.trip_requests.Response
import com.example.features.trip_requests.repository.TripRequestRepositoryImpl
import com.example.features.trips.CreateTripRequest
import com.example.features.trips.TripDetailedResponse
import com.example.features.trips.TripResponse
import com.example.features.trips.UpdateTripRequest
import com.example.features.trips.repository.TripRepositoryImpl
import com.example.features.users.UserResponse
import com.example.features.users.domain.UserService
import com.example.utils.extensions.toBookedTripsTimeRange
import com.example.utils.extensions.toCreatedTripsTimeRange
import com.example.utils.extensions.toRequestsTimeRange
import com.example.utils.validator.TimeValidator
import java.time.Instant

object TripService {
    fun fetchTrips(fromCityId: Long, toCityId: Long, date: String): List<TripResponse> {
        val tripsDTO = TripRepositoryImpl.findTripsByParameters(fromCityId, toCityId, date)
        val tripsMap = mutableMapOf<TripDTO, UserResponse?>()
        for (tripDTO in tripsDTO) {
            tripsMap[tripDTO] = UserService.fetchUserByToken(tripDTO.driverUuid)
        }
        val trips = mutableListOf<TripResponse>()
        for ((trip, driver) in tripsMap) {
            val startLocation = LocationService.fetchLocationById(trip.startLocationId)
            val endLocation = LocationService.fetchLocationById(trip.endLocationId)
            if (driver != null && startLocation != null && endLocation != null) {
                trips.add(
                    trip.toTripResponse(driver, startLocation, endLocation)
                )
            }
        }
        return trips
    }

    fun fetchDetailedTripById(tripId: Long): TripDetailedResponse? {
        val trip = TripRepositoryImpl.findByTripId(tripId) ?: return null
        val driver = UserService.fetchUserByToken(trip.driverUuid) ?: return null
        val startLocation = LocationService.fetchLocationById(trip.startLocationId) ?: return null
        val endLocation = LocationService.fetchLocationById(trip.endLocationId) ?: return null
        val car: CarDTO?
        if (driver.carId != null) {
            car = CarService.fetchCarById(driver.carId) ?: return null
        } else {
            return null
        }
        return trip.toTripDetailedResponse(driver, startLocation, endLocation, car)
    }

    fun fetchTripsByDriverUuid(driverUuid: String): List<TripResponse> {
        val tripsDTO = TripRepositoryImpl.findTripsByDriverUuid(driverUuid)
        val tripsMap = mutableMapOf<TripDTO, UserResponse?>()
        for (tripDTO in tripsDTO) {
            tripsMap[tripDTO] = UserService.fetchUserByToken(tripDTO.driverUuid)
        }
        val trips = mutableListOf<TripResponse>()
        for ((trip, driver) in tripsMap) {
            val startLocation = LocationService.fetchLocationById(trip.startLocationId)
            val endLocation = LocationService.fetchLocationById(trip.endLocationId)
            if (driver != null && startLocation != null && endLocation != null) {
                trips.add(
                    trip.toTripResponse(driver, startLocation, endLocation)
                )
            }
        }
        return trips
    }

    fun fetchTripsByPassengerUuid(passengerUuid: String): List<TripResponse> {
        val passengerTripsList = TripPassengerRepositoryImpl.findPassengersByPassengerUuid(passengerUuid)
        val tripsIdList = passengerTripsList.map { it.tripId }
        val tripsDTO = mutableListOf<TripDTO?>()
        for(tripId in tripsIdList) {
            tripsDTO.add(TripRepositoryImpl.findByTripId(tripId))
        }
        val filteredTripsDTO = tripsDTO.filterNotNull()
        val tripsMap = mutableMapOf<TripDTO, UserResponse?>()
        for (tripDTO in filteredTripsDTO) {
            tripsMap[tripDTO] = UserService.fetchUserByToken(tripDTO.driverUuid)
        }
        val trips = mutableListOf<TripResponse>()
        for ((trip, driver) in tripsMap) {
            val startLocation = LocationService.fetchLocationById(trip.startLocationId)
            val endLocation = LocationService.fetchLocationById(trip.endLocationId)
            if (driver != null && startLocation != null && endLocation != null) {
                trips.add(
                    trip.toTripResponse(driver, startLocation, endLocation)
                )
            }
        }
        return trips
    }

    fun fetchTripsByRequesterUuid(requesterUuid: String): List<TripResponse> {
        val requesterTripsList = TripRequestRepositoryImpl.findAllByUserUuid(requesterUuid)
        val tripsIdList = requesterTripsList.map { it.tripId }
        val tripsDTO = mutableListOf<TripDTO?>()
        for(tripId in tripsIdList) {
            tripsDTO.add(TripRepositoryImpl.findByTripId(tripId))
        }
        val filteredTripsDTO = tripsDTO.filterNotNull()
        val tripsMap = mutableMapOf<TripDTO, UserResponse?>()
        for (tripDTO in filteredTripsDTO) {
            tripsMap[tripDTO] = UserService.fetchUserByToken(tripDTO.driverUuid)
        }
        val trips = mutableListOf<TripResponse>()
        for ((trip, driver) in tripsMap) {
            val startLocation = LocationService.fetchLocationById(trip.startLocationId)
            val endLocation = LocationService.fetchLocationById(trip.endLocationId)
            if (driver != null && startLocation != null && endLocation != null) {
                trips.add(
                    trip.toTripResponse(driver, startLocation, endLocation)
                )
            }
        }
        return trips
    }

    fun updateTrip(tripId: Long, request: UpdateTripRequest): Response {
        return if(TripRepositoryImpl.updateTrip(tripId, request)) {
            Response(
                message = "Поїздку успішно оновлено",
                isSuccess = true
            )
        } else {
            Response(
                message = "Помилка при оновлені поїздки",
                isSuccess = false
            )
        }
    }

    fun createTrip(tripRequest: CreateTripRequest): Response {
        val startTime = Instant.parse(tripRequest.startTime)
        val endTime = Instant.parse(tripRequest.endTime)

        val myRequests = TripRequestRepositoryImpl.findAllByUserUuid(tripRequest.driverUuid).filter {
            it.status == TripRequestStatus.PENDING.type || it.status == TripRequestStatus.ACCEPTED.type
        }
        val myBookedTrips = TripPassengerRepositoryImpl.findPassengersByPassengerUuid(tripRequest.driverUuid)
        val myCreatedTrips = TripRepositoryImpl.findTripsByDriverUuid(tripRequest.driverUuid)

        val myRequestsTimeRange = mutableListOf<Pair<Instant, Instant>>().toRequestsTimeRange(myRequests)
        val myBookedTripsTimeRange = mutableListOf<Pair<Instant, Instant>>().toBookedTripsTimeRange(myBookedTrips)
        val myCreatedTripsTimeRange = mutableListOf<Pair<Instant, Instant>>().toCreatedTripsTimeRange(myCreatedTrips)

       if(myRequestsTimeRange.isNotEmpty() &&
           !TimeValidator.isTripTimeAvailable(startTime, endTime, myRequestsTimeRange)) {
           return Response(
               isSuccess = false,
               message = "На таку годину вже є запит на поїздку"
           )
       }
        if(myBookedTripsTimeRange.isNotEmpty() &&
            !TimeValidator.isTripTimeAvailable(startTime, endTime, myBookedTripsTimeRange)) {
            return Response(
                isSuccess = false,
                message = "На таку годину вже є заброньована поїздка"
            )
        }
        if(myCreatedTripsTimeRange.isNotEmpty() &&
            !TimeValidator.isTripTimeAvailable(startTime, endTime, myCreatedTripsTimeRange)) {
            return Response(
                isSuccess = false,
                message = "На таку годину вже була створена поїздка раніше"
            )
        }
        if(!TripRepositoryImpl.saveTrip(tripRequest)) {
            return Response(
                isSuccess = false,
                message = "Помилка створення поїздки"
            )
        }
        return Response(
            isSuccess = true,
            message = "Поїздку було створено"
        )
    }

    fun deleteTrip(tripId: Long): Response {
        if(TripRepositoryImpl.deleteTrip(tripId)) {
            return Response(
                isSuccess = true,
                message = "Поїздку видалено успішно"
            )
        }
        return Response(
            isSuccess = false,
            message = "Помилка видалення поїздки"
        )
    }
}