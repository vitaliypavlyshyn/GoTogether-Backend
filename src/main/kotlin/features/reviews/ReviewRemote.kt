package com.example.features.reviews

import com.example.database.reviews.toReviewResponse
import com.example.database.reviews.toReviewsResponse
import kotlinx.serialization.Serializable

@Serializable
data class ReviewResponse(
    val reviewId: Long,
    val tripId: Long,
    val reviewerUuid: String,
    val reviewedUserUuid: String,
    val rating: Int?,
    val drivingSkills: Int?,
    val comment: String,
    val createdAt: String
)

@Serializable
data class DetailedReviewResponse(
    val reviewId: Long,
    val tripId: Long,
    val reviewerUuid: String,
    val reviewerFirstName: String,
    val reviewerPicture: ByteArray?,
    val reviewedUserUuid: String,
    val rating: Int?,
    val drivingSkills: Int?,
    val comment: String,
    val createdAt: String
)

@Serializable
data class RatingResponse(
    val avgRating: Double?,
    val avgDrivingSkills: Double?,
    val countReviews: Int
)


@Serializable
data class CreateReviewRequest(
    val tripId: Long,
    val reviewerUuid: String,
    val reviewedUserUuid: String,
    val rating: Int,
    val drivingSkills: Int? = null,
    val comment: String,
)

