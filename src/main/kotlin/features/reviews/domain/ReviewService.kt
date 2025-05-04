package com.example.features.reviews.domain

import com.example.database.reviews.ReviewDTO
import com.example.database.users.Users
import com.example.features.reviews.CreateReviewRequest
import com.example.features.reviews.repository.ReviewRepositoryImpl
import com.example.features.trip_requests.Response
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object ReviewService {
    fun fetchReviewsByReviewedUuid(userUuid: String): List<ReviewDTO>? {
        transaction {
            Users.select { Users.userUuid eq userUuid }
                .singleOrNull()
                ?.get(Users.userUuid)
        } ?: return null
        return ReviewRepositoryImpl.findAllByReviewedUuid(userUuid)
    }

    fun fetchReviewsByReviewerUuid(userUuid: String): List<ReviewDTO>? {
        transaction {
            Users.select { Users.userUuid eq userUuid }
                .singleOrNull()
                ?.get(Users.userUuid)
        } ?: return null
        return ReviewRepositoryImpl.findAllByReviewerUuid(userUuid)
    }

    fun saveReview(request: CreateReviewRequest): Response {

        return if(ReviewRepositoryImpl.saveReview(request)) {
            Response(
                isSuccess = true,
                message = "Відгук створено успішно"
            )
        } else {
            Response(
                isSuccess = false,
                message = "Помилка створення відгуку"
            )
        }
    }
}