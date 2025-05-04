package com.example.database.reviews

import com.example.database.cars.toCarResponse
import com.example.database.cars.toCarsResponse
import com.example.features.cars.CarResponse
import com.example.features.reviews.DetailedReviewResponse
import com.example.features.reviews.RatingResponse
import com.example.features.reviews.ReviewResponse
import com.example.features.users.UserResponse
import java.time.Instant

data class ReviewDTO(
    val reviewId: Long,
    val tripId: Long,
    val reviewerUuid: String,
    val reviewedUserUuid: String,
    val rating: Int?,
    val drivingSkills: Int?,
    val comment: String,
    val createdAt: Instant
)

fun ReviewDTO.toReviewResponse(): ReviewResponse {
    return ReviewResponse(
        reviewId = reviewId,
        tripId = tripId,
        reviewerUuid = reviewerUuid,
        reviewedUserUuid = reviewedUserUuid,
        rating = rating,
        drivingSkills = drivingSkills,
        comment = comment,
        createdAt = createdAt.toString()
    )
}

fun List<ReviewDTO>.toReviewsResponse(): List<ReviewResponse> {
    val reviewsResponse = mutableListOf<ReviewResponse>()
    for(i in this) {
        reviewsResponse.add(i.toReviewResponse())
    }
    return reviewsResponse
}

fun List<ReviewDTO>.toRatingResponse(): RatingResponse {
    val ratings = this.mapNotNull { it.rating }
    val drivingSkills = this.mapNotNull { it.drivingSkills }

    return RatingResponse(
        avgRating = if (ratings.isNotEmpty()) ratings.average() else null,
        avgDrivingSkills = if (drivingSkills.isNotEmpty()) drivingSkills.average() else null,
        countReviews = this.size
    )
}

fun ReviewDTO.toDetailedReviewResponse(userResponse: UserResponse) : DetailedReviewResponse {
    return DetailedReviewResponse(
        reviewId = reviewId,
        tripId = tripId,
        reviewerUuid = reviewerUuid,
        reviewerFirstName = userResponse.firstName,
        reviewerPicture = userResponse.pictureProfile,
        reviewedUserUuid = reviewedUserUuid,
        rating = rating,
        drivingSkills = drivingSkills,
        comment = comment,
        createdAt = createdAt.toString()
    )
}

fun List<ReviewDTO>.toDetailedReviewsResponse(users: MutableList<UserResponse>): List<DetailedReviewResponse> {
    val reviewsResponse = mutableListOf<DetailedReviewResponse>()
    for(i in this.indices) {
        reviewsResponse.add(this[i].toDetailedReviewResponse(users[i]))
    }
    return reviewsResponse
}