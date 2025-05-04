package com.example.database.reviews

import com.example.database.trips.Trips
import com.example.database.users.Users
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object Reviews: Table("reviews") {
    val reviewId = long("review_id").autoIncrement()
    val tripId = long("trip_id").references(
        Trips.tripId, onDelete = ReferenceOption.CASCADE
    )
    val reviewerUuid = varchar("reviewer_uuid", 50)
        .references(Users.userUuid, onDelete = ReferenceOption.CASCADE)
    val reviewedUserUuid = varchar("reviewed_user_uuid", 50)
        .references(Users.userUuid, onDelete = ReferenceOption.CASCADE)
    val rating = integer("rating").nullable()
    val drivingSkills = integer("driving_skills").nullable()
    val comment = varchar("comment", 150)
    val createdAt = timestamp("created_at")

    override val primaryKey = PrimaryKey(reviewId)
}