package com.example.database.reviews

import kotlinx.datetime.toJavaInstant
import org.jetbrains.exposed.sql.ResultRow

fun mapToReviewDTO(row: ResultRow): ReviewDTO {
    return ReviewDTO(
        reviewId = row[Reviews.reviewId],
        tripId = row[Reviews.tripId],
        reviewerUuid = row[Reviews.reviewerUuid],
        reviewedUserUuid = row[Reviews.reviewedUserUuid],
        rating = row[Reviews.rating],
        drivingSkills = row[Reviews.drivingSkills],
        comment = row[Reviews.comment],
        createdAt = row[Reviews.createdAt].toJavaInstant()
    )
}