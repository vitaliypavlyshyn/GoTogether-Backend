package com.example.features.trip_passengers.domain

import com.example.database.trip_passengers.toTripPassengerResponse
import com.example.database.trip_requests.TripRequestStatus
import com.example.features.trip_passengers.CreateTripPassengerRequest
import com.example.features.trip_passengers.TripPassengerResponse
import com.example.features.trip_passengers.repository.TripPassengerRepositoryImpl
import com.example.features.trip_requests.Response
import com.example.features.trip_requests.repository.TripRequestRepositoryImpl
import com.example.features.trips.UpdateTripRequest
import com.example.features.trips.repository.TripRepositoryImpl
import com.example.features.users.domain.UserService
import com.example.utils.extensions.toBookedTripsTimeRange
import com.example.utils.extensions.toCreatedTripsTimeRange
import com.example.utils.extensions.toRequestsTimeRange
import com.example.utils.validator.TimeValidator
import java.time.Instant

object TripPassengerService {
    fun fetchPassengersByTripId(tripId: Long): List<TripPassengerResponse>{
        val tripPassengers = TripPassengerRepositoryImpl.findPassengersByTripId(tripId)
        val users = UserService.fetchAllUsers()
        val response = mutableListOf<TripPassengerResponse>()
        for(passenger in tripPassengers) {
            for(user in users) {
                if(passenger.passengerUuid == user.userUuid) {
                    response.add(
                        passenger.toTripPassengerResponse(user)
                    )
                }
            }
        }
        return response
    }



    fun createPassenger(tripPassengerRequest: CreateTripPassengerRequest): Response {
        val currentTrip = TripRepositoryImpl.findByTripId(tripPassengerRequest.tripId)
        if(currentTrip != null) {
            val startTime = currentTrip.startTime
            val endTime = currentTrip.endTime

            val myRequests = TripRequestRepositoryImpl.findAllByUserUuid(tripPassengerRequest.passengerUuid).filter {
                it.status == TripRequestStatus.PENDING.type || it.status == TripRequestStatus.ACCEPTED.type
            }
            val myBookedTrips = TripPassengerRepositoryImpl.findPassengersByPassengerUuid(tripPassengerRequest.passengerUuid)
            val myCreatedTrips = TripRepositoryImpl.findTripsByDriverUuid(tripPassengerRequest.passengerUuid)

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
            if (!TripPassengerRepositoryImpl.savePassenger(tripPassengerRequest)) {
                return Response(
                    isSuccess = false,
                    message = "Помилка бронювання"
                )
            }
            if(currentTrip.availableSeats < tripPassengerRequest.seatsBooked) {
                return Response(
                    isSuccess = false,
                    message = "Недостатньо вільних місць"
                )
            }
            return Response(
                isSuccess = true,
                message = "Поїздку заброньовано"
            )
        }
        return Response(
            isSuccess = false,
            message = "Такої поїздки не існує!"
        )
    }

    fun deletePassenger(tripId: Long, passengerUuid: String): Response {
        val passenger = TripPassengerRepositoryImpl.findPassengerByTripIdAndPassengerUuid(tripId, passengerUuid)
            ?: return Response(
                message = "Пасажира не знайдено",
                isSuccess = false
            )

        val deleteSuccess = TripPassengerRepositoryImpl.deletePassenger(tripId, passengerUuid)
        if (!deleteSuccess) {
            return Response(
                message = "Помилка видалення пасажира",
                isSuccess = false
            )
        }

        val trip = TripRepositoryImpl.findByTripId(tripId)
            ?: return Response(
                message = "Поїздку не знайдено",
                isSuccess = false
            )

        val updateSuccess = TripRepositoryImpl.updateTrip(
            tripId,
            request = UpdateTripRequest(
                availableSeats = trip.availableSeats + passenger.seatsBooked
            )
        )

        if (!updateSuccess) {
            return Response(
                message = "Помилка оновлення поїздки",
                isSuccess = false
            )
        }

        //TripRepositoryImpl.updateTrip(tripId, )
        return Response(
            message = "Бронювання скасовано",
            isSuccess = true
        )
    }
}