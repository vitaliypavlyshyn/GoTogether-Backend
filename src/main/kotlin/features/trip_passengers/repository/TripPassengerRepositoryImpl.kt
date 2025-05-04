package com.example.features.trip_passengers.repository

import com.example.database.auth.Auth
import com.example.database.trip_passengers.TripPassengerDTO
import com.example.database.trip_passengers.TripPassengers
import com.example.database.trip_passengers.mapToTripPassengerDTO
import com.example.database.trips.TripStatus
import com.example.database.trips.Trips
import com.example.database.users.Users
import com.example.features.trip_passengers.CreateTripPassengerRequest
import com.example.features.trips.CreateTripRequest
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

object TripPassengerRepositoryImpl: TripPassengerRepository {
    override fun findPassengersByTripId(tripId: Long): List<TripPassengerDTO> {
        return transaction {
            TripPassengers.select{TripPassengers.tripId eq tripId}.map {
                mapToTripPassengerDTO(it)
            }
        }
    }

    override fun findPassengersByPassengerUuid(passengerUuid: String): List<TripPassengerDTO> {
        return transaction {
            TripPassengers.select{TripPassengers.passengerUuid eq passengerUuid}.map {
                mapToTripPassengerDTO(it)
            }
        }
    }

    override fun findPassengerByTripIdAndPassengerUuid(tripId: Long, passengerUuid: String): TripPassengerDTO? {
        return transaction {
            TripPassengers.select{  (TripPassengers.tripId eq tripId) and  (TripPassengers.passengerUuid eq passengerUuid)}.map {
                mapToTripPassengerDTO(it)
            }.singleOrNull()
        }
    }

    override fun savePassenger(tripPassengerRequest: CreateTripPassengerRequest): Boolean {
        return try {
            transaction {
                val result  = TripPassengers.insert {
                    it[tripId] = tripPassengerRequest.tripId
                    it[seatsBooked] = tripPassengerRequest.seatsBooked
                    it[passengerUuid] = tripPassengerRequest.passengerUuid
                    it[createdAt] = Instant.now().toKotlinInstant()
                }
                result.insertedCount > 0 || result.resultedValues?.isNotEmpty() == true
            }
        } catch (e: Exception) {
            false
        }
    }

    override fun deletePassenger(tripId: Long, passengerUuid: String): Boolean {
        return transaction {
            val deletedRows = TripPassengers.deleteWhere {
                (TripPassengers.tripId eq tripId) and  (TripPassengers.passengerUuid eq passengerUuid)
            }

             deletedRows > 0
        }
    }
}