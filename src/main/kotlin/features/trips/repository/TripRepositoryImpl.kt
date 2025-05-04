package com.example.features.trips.repository

import com.example.database.trip_requests.TripRequests
import com.example.database.trips.TripDTO
import com.example.database.trips.TripStatus
import com.example.database.trips.Trips
import com.example.database.trips.mapToTripDTO
import com.example.features.trips.CreateTripRequest
import com.example.features.trips.UpdateTripRequest
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

object TripRepositoryImpl : TripRepository {
    override fun findTripsByParameters(fromCityId: Long, toCityId: Long, date: String): List<TripDTO> {
        val localDate = LocalDate.parse(date)
        val startOfDay = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
        val endOfDay = localDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()
        return transaction {
            Trips.select {
                (Trips.startLocationId eq fromCityId) and
                        (Trips.endLocationId eq toCityId) and
                        (Trips.startTime greaterEq startOfDay.toKotlinInstant()) and
                        (Trips.startTime less endOfDay.toKotlinInstant())
            }.map {
                mapToTripDTO(it)
            }
        }
    }

    override fun findTripsByDriverUuid(driverUuid: String): List<TripDTO> {
        return transaction {
            Trips.select { Trips.driverUuid eq driverUuid }.map {
                mapToTripDTO(it)
            }
        }
    }

    override fun findByTripId(id: Long): TripDTO? {
        return transaction {
            Trips.select { Trips.tripId eq id }.map {
                mapToTripDTO(it)
            }.singleOrNull()
        }
    }

    override fun findAll(): List<TripDTO> {
        return transaction {
            Trips.selectAll().map {
                mapToTripDTO(it)
            }
        }
    }

    override fun saveTrip(request: CreateTripRequest): Boolean {
        return try {
            transaction {
                val result = Trips.insert {
                    it[driverUuid] = request.driverUuid
                    it[startLocationId] = request.startLocationId
                    it[endLocationId] = request.endLocationId
                    it[startTime] = Instant.parse(request.startTime).toKotlinInstant()
                    it[endTime] = Instant.parse(request.endTime).toKotlinInstant()
                    it[distanceInMeters] = request.distanceInMeters
                    it[availableSeats] = request.availableSeats
                    it[status] = TripStatus.SCHEDULED.type
                    it[price] = request.price
                    it[isFastConfirm] = request.isFastConfirm
                    it[createdAt] = Instant.now().toKotlinInstant()
                    it[updatedAt] = Instant.now().toKotlinInstant()
                }
                result.insertedCount > 0 || result.resultedValues?.isNotEmpty() == true
            }
        } catch (e: Exception) {
            false
        }
    }

    override fun updateTrip(tripId: Long, request: UpdateTripRequest): Boolean {
        return transaction {
            val updatedRows = Trips.update({ Trips.tripId eq tripId }) {
                var hasUpdate = false

                request.availableSeats?.let { it1 -> it[availableSeats] = it1; hasUpdate = true }
                request.status?.let { it2 -> it[status] = it2; hasUpdate = true }
                it[updatedAt] = Instant.now().toKotlinInstant()

                    if (!hasUpdate) error("Nothing to update")
            }

            updatedRows > 0
        }
    }

    override fun deleteTrip(tripId: Long): Boolean {
        return transaction {
            val deletedRows = Trips.deleteWhere {
                Trips.tripId eq tripId
            }

            deletedRows > 0
        }
    }

}