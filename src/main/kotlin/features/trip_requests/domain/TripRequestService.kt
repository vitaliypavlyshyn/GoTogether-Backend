package com.example.features.trip_requests.domain

import com.example.database.trip_requests.TripRequestStatus
import com.example.database.trip_requests.toTripRequestResponse
import com.example.database.trip_requests.toTripRequestsResponse
import com.example.database.users.Users
import com.example.features.trip_passengers.repository.TripPassengerRepositoryImpl
import com.example.features.trip_requests.*
import com.example.features.trip_requests.repository.TripRequestRepositoryImpl
import com.example.features.trips.repository.TripRepositoryImpl
import com.example.features.users.repository.UserRepositoryImpl
import com.example.utils.extensions.toBookedTripsTimeRange
import com.example.utils.extensions.toCreatedTripsTimeRange
import com.example.utils.extensions.toRequestsTimeRange
import com.example.utils.validator.TimeValidator
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

object TripRequestService {
    fun fetchRequestsByUserUuid(userUuid: String): List<TripRequestResponse>? {
        transaction {
            Users.select { Users.userUuid eq userUuid }
                .singleOrNull()
                ?.get(Users.userUuid)
        } ?: return null
        val tripRequestDTO = TripRequestRepositoryImpl.findAllByUserUuid(userUuid)
        return tripRequestDTO.toTripRequestsResponse()
    }

    fun fetchRequestsByTripId(tripId: Long): List<TripRequestResponse> {
        val tripRequests = TripRequestRepositoryImpl.findAllByTripId(tripId)
        val tripRequestsResponse = mutableListOf<TripRequestResponse>()
        for (request in tripRequests) {
            val passenger = UserRepositoryImpl.findByUuid(request.passengerUuid)
            if(passenger != null) {
                tripRequestsResponse.add(
                    request.toTripRequestResponse(passenger)
                )
            }
        }
        return tripRequestsResponse
    }

    fun fetchRequestByTripIdAndUserUuid(tripId: Long, userUuid: String): TripRequestResponse? {
        val tripRequest = TripRequestRepositoryImpl.findRequestByTripIdAndUSerUuid(tripId, userUuid) ?: return null
        val user = UserRepositoryImpl.findByUuid(userUuid) ?: return null
        return tripRequest.toTripRequestResponse(user)
    }

    fun updateRequest(requestId: Long, request: UpdateTripRequestRequest): Response {
        return if (TripRequestRepositoryImpl.updateRequest(requestId, request)) {
            Response(
                isSuccess = true,
                message = "Запит успішно оновлено"
            )
        } else {
            Response(
                isSuccess = false,
                message = "Помилка при оновлені"
            )
        }
    }

    fun createRequest(tripRequest: CreateTripRequestRequest): Response {
        val currentTrip = TripRepositoryImpl.findByTripId(tripRequest.tripId)
        if(currentTrip != null) {
            val startTime = currentTrip.startTime
            val endTime = currentTrip.endTime

            val myRequests = TripRequestRepositoryImpl.findAllByUserUuid(tripRequest.passengerUuid).filter {
                it.status == TripRequestStatus.PENDING.type || it.status == TripRequestStatus.ACCEPTED.type
            }
            val myBookedTrips = TripPassengerRepositoryImpl.findPassengersByPassengerUuid(tripRequest.passengerUuid)
            val myCreatedTrips = TripRepositoryImpl.findTripsByDriverUuid(tripRequest.passengerUuid)

            val myRequestsTimeRange = mutableListOf<Pair<Instant, Instant>>().toRequestsTimeRange(myRequests)
            val myBookedTripsTimeRange = mutableListOf<Pair<Instant, Instant>>().toBookedTripsTimeRange(myBookedTrips)
            val myCreatedTripsTimeRange =
                mutableListOf<Pair<Instant, Instant>>().toCreatedTripsTimeRange(myCreatedTrips)

            if (myRequestsTimeRange.isNotEmpty() &&
                !TimeValidator.isTripTimeAvailable(startTime, endTime, myRequestsTimeRange)
            ) {
                return Response(
                    isSuccess = false,
                    message = "На таку годину вже є запит на поїздку"
                )
            }
            if (myBookedTripsTimeRange.isNotEmpty() &&
                !TimeValidator.isTripTimeAvailable(startTime, endTime, myBookedTripsTimeRange)
            ) {
                return Response(
                    isSuccess = false,
                    message = "На таку годину вже є заброньована поїздка"
                )
            }
            if (myCreatedTripsTimeRange.isNotEmpty() &&
                !TimeValidator.isTripTimeAvailable(startTime, endTime, myCreatedTripsTimeRange)
            ) {
                return Response(
                    isSuccess = false,
                    message = "На таку годину вже була створена поїздка раніше"
                )
            }
            if (!TripRequestRepositoryImpl.saveRequest(tripRequest)) {
                return Response(
                    isSuccess = false,
                    message = "Помилка створення запиту"
                )
            }
            if(currentTrip.availableSeats < tripRequest.requestedSeats) {
                return Response(
                    isSuccess = false,
                    message = "Недостатньо вільних місць"
                )
            }
            return Response(
                isSuccess = true,
                message = "Запит на поїздку було створено успішно"
            )
        }
        return Response(
            isSuccess = false,
            message = "Такої поїздки не існує!"
        )
    }

    fun deleteRequest(requestId: Long): Response {
        if(TripRequestRepositoryImpl.deleteRequest(requestId)) {
            return Response(
                isSuccess = true,
                message = "Обліковий запис видалено успішно"
            )
        }
        return Response(
            isSuccess = false,
            message = "Помилка видалення облікового запису"
        )
    }
}