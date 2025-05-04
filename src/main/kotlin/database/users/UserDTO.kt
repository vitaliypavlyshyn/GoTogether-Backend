package com.example.database.users

import com.example.database.auth.AuthDTO
import com.example.database.cars.CarDTO
import com.example.features.reviews.RatingResponse
import com.example.features.users.UserResponse


data class UserDTO(
    val userUuid: String,
    val carId: Long?,
    val pictureProfile: ByteArray?,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String,
    val phoneNumber: String?,
    val description: String?,
    val isDeleted: Boolean
)


fun UserDTO.toUserResponse(ratingResponse: RatingResponse, carDTO: CarDTO?, authDTO: AuthDTO?): UserResponse {
    return UserResponse(
        userUuid = this.userUuid,
        carId = this.carId,
        email = authDTO?.email ?: "",
        make = carDTO?.make,
        model = carDTO?.model,
        pictureProfile = this.pictureProfile,
        firstName = this.firstName,
        lastName = this.lastName,
        dateOfBirth = this.dateOfBirth,
        phoneNumber = this.phoneNumber,
        description = this.description,
        avgRating = ratingResponse.avgRating,
        avgDrivingSkills = ratingResponse.avgDrivingSkills,
        countReviews = ratingResponse.countReviews,
        isDeleted = this.isDeleted,
        createdAt = authDTO?.createdAt.toString()
    )
}
