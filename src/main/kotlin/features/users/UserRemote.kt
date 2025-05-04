package com.example.features.users

import com.example.database.users.Users
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import java.time.Instant

@Serializable
data class UserResponse(
    val userUuid: String,
    val carId: Long?,
    val email: String?,
    val make: String?,
    val model: String?,
    val pictureProfile: ByteArray?,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String,
    val phoneNumber: String?,
    val description: String?,
    val avgRating: Double?,
    val avgDrivingSkills: Double?,
    val countReviews: Int,
    val isDeleted: Boolean,
    val createdAt: String?
)

@Serializable
data class UpdateUserRequest(
    val carId: Long? = null,
    val pictureProfile: ByteArray? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val dateOfBirth: String? = null,
    val phoneNumber: String? = null,
    val description: String? = null,
    val isDeleted: Boolean = false
)

fun UpdateUserRequest.applyToUpdate(stmt: UpdateBuilder<*>) {
    carId?.let { stmt[Users.carId] = it }
    pictureProfile?.let { stmt[Users.pictureProfile] = it }
    firstName?.let { stmt[Users.firstName] = it }
    lastName?.let { stmt[Users.lastName] = it }
    dateOfBirth?.let {
        val parsedDate = LocalDate.parse(it)
        stmt[Users.dateOfBirth] = parsedDate
    }
    phoneNumber?.let { stmt[Users.phoneNumber] = it }
    description?.let { stmt[Users.description] = it }
    isDeleted.let { stmt[Users.isDeleted] = it }
}

