package com.example.database.trip_requests

import com.example.database.trips.Trips
import com.example.database.users.Users
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object TripRequests : Table("trip_requests") {
    val requestId = long("request_id").autoIncrement()
    val tripId = long("trip_id")
        .references(Trips.tripId, onDelete = ReferenceOption.CASCADE)
    val passengerUuid = varchar("passenger_uuid", 50)
        .references(Users.userUuid, onDelete = ReferenceOption.CASCADE)
    val requestedSeats = integer("requested_seats").default(1)
    val status = varchar("status", 30)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")

    override val primaryKey = PrimaryKey(requestId)
}