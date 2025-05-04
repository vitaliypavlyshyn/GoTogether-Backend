package com.example.features.trip_requests.repository

import com.example.database.auth.Auth
import com.example.database.trip_passengers.TripPassengers
import com.example.database.trip_requests.TripRequestDTO
import com.example.database.trip_requests.TripRequestStatus
import com.example.database.trip_requests.TripRequests
import com.example.database.trip_requests.mapToTripRequestDTO
import com.example.database.trips.TripStatus
import com.example.database.trips.Trips
import com.example.database.users.Users
import com.example.features.trip_requests.CreateTripRequestRequest
import com.example.features.trip_requests.UpdateTripRequestRequest
import com.example.features.trips.CreateTripRequest
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

object TripRequestRepositoryImpl: TripRequestRepository {
    override fun findAllByUserUuid(userUuid: String): List<TripRequestDTO> {
        return transaction {
            TripRequests.select{TripRequests.passengerUuid eq userUuid}.map {
                mapToTripRequestDTO(it)
            }
        }
    }

    override fun findAllByTripId(tripId: Long): List<TripRequestDTO> {
        return transaction {
            TripRequests.select{TripRequests.tripId eq tripId}.map {
                mapToTripRequestDTO(it)
            }
        }
    }

    override fun findRequestByTripIdAndUSerUuid(tripId: Long, userUuid: String): TripRequestDTO? {
        return transaction {
            TripRequests.select{(TripRequests.tripId eq tripId) and
                    (TripRequests.passengerUuid eq userUuid)}.map {
                mapToTripRequestDTO(it)
            }.singleOrNull()
        }
    }

    override fun findRequestByRequestId(requestId: Long): TripRequestDTO? {
        return transaction {
            TripRequests.select{TripRequests.requestId eq requestId}.map {
                mapToTripRequestDTO(it)
            }.singleOrNull()
        }
    }

    override fun saveRequest(request: CreateTripRequestRequest): Boolean {
        return try {
            transaction {
                val result  = TripRequests.insert {
                    it[tripId] = request.tripId
                    it[passengerUuid] = request.passengerUuid
                    it[requestedSeats] = request.requestedSeats
                    it[status] = TripRequestStatus.PENDING.type
                    it[createdAt] = Instant.now().toKotlinInstant()
                    it[updatedAt] = Instant.now().toKotlinInstant()
                }
                result.insertedCount > 0 || result.resultedValues?.isNotEmpty() == true
            }
        } catch (e: Exception) {
            false
        }
    }

    override fun deleteRequest(requestId: Long): Boolean {
        return transaction {
            val deletedRows = TripRequests.deleteWhere {
                TripRequests.requestId eq requestId
            }

           deletedRows > 0
        }
    }

    override fun updateRequest(requestId: Long, request: UpdateTripRequestRequest): Boolean {
        return transaction {
            val updatedRows = TripRequests.update({ TripRequests.requestId eq requestId }) {
                var hasUpdate = false

                request.status.let { it2 -> it[status] = it2.toString(); hasUpdate = true }
                it[updatedAt] = Instant.now().toKotlinInstant()

                if (!hasUpdate) error("Nothing to update")
            }

            if(request.status == TripRequestStatus.ACCEPTED.type) {
                val requestDTO = TripRequests.select(TripRequests.requestId eq requestId).map {
                    mapToTripRequestDTO(it)
                }.single()
                TripPassengers.insert {
                    it[passengerUuid] = requestDTO.passengerUuid
                    it[tripId] = requestDTO.tripId
                    it[seatsBooked] = requestDTO.requestedSeats
                    it[createdAt] = Instant.now().toKotlinInstant()
                }
            }

            updatedRows > 0
        }
    }
}