package com.example.features.reviews.repository

import com.example.database.reviews.ReviewDTO
import com.example.database.reviews.Reviews
import com.example.database.reviews.mapToReviewDTO
import com.example.database.trip_passengers.TripPassengers
import com.example.features.reviews.CreateReviewRequest
import com.example.features.reviews.domain.ReviewService
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

object ReviewRepositoryImpl: ReviewRepository {
    override fun findAllByReviewedUuid(userUuid: String): List<ReviewDTO> {
        return transaction {
            Reviews.select{Reviews.reviewedUserUuid eq userUuid}.map {
                mapToReviewDTO(it)
            }
        }
    }

    override fun findAllByReviewerUuid(userUuid: String): List<ReviewDTO> {
        return transaction {
            Reviews.select{Reviews.reviewerUuid eq userUuid}.map {
                mapToReviewDTO(it)
            }
        }
    }


    override fun saveReview(request: CreateReviewRequest): Boolean {
        return try {
            transaction {
                val result  = Reviews.insert {
                    it[tripId] = request.tripId
                    it[reviewerUuid] = request.reviewerUuid
                    it[reviewedUserUuid] = request.reviewedUserUuid
                    it[rating] = request.rating
                    it[drivingSkills] = request.drivingSkills
                    it[comment] = request.comment
                    it[createdAt] = Instant.now().toKotlinInstant()
                }
                result.insertedCount > 0 || result.resultedValues?.isNotEmpty() == true
            }
        } catch (e: Exception) {
            false
        }
    }
}