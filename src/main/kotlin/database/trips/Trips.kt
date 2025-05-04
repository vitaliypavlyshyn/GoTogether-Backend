package com.example.database.trips

import com.example.database.cities.Cities
import com.example.database.users.Users
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object Trips: Table("trips") {
    val tripId = long("trip_id").autoIncrement()
    val driverUuid = varchar("driver_uuid", 50)
        .references(Users.userUuid)
    val startLocationId = long("start_location_id")
        .references(Cities.cityId)
    val endLocationId = long("end_location_id")
        .references(Cities.cityId)
    val startTime = timestamp("start_time")
    val endTime = timestamp("end_time")
    val distanceInMeters = integer("distance_in_meters")
    val availableSeats = integer("available_seats").default(4)
    val status = varchar("status", 30)
    val price = integer("price")
    val isFastConfirm = bool("is_fast_confirm").default(true)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")

    override val primaryKey = PrimaryKey(tripId)
}