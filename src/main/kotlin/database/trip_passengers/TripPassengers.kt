package com.example.database.trip_passengers

import com.example.database.trips.Trips
import com.example.database.users.Users
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object TripPassengers: Table("trip_passengers") {
    val tripPassengerId = long("trip_passenger_id").autoIncrement()
    val tripId = long("trip_id")
        .references(Trips.tripId, onDelete = ReferenceOption.CASCADE)
    val passengerUuid = varchar("passenger_uuid", 50)
        .references(Users.userUuid, onDelete = ReferenceOption.CASCADE)
    val seatsBooked = integer("seats_booked")
    val createdAt = timestamp("created_at")

    override val primaryKey = PrimaryKey(tripPassengerId)
}