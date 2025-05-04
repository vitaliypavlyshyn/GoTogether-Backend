package com.example.features.reviews.repository

import com.example.database.reviews.ReviewDTO
import com.example.features.reviews.CreateReviewRequest

interface ReviewRepository {
    fun findAllByReviewedUuid(userUuid: String): List<ReviewDTO>
    fun findAllByReviewerUuid(userUuid: String): List<ReviewDTO>
    fun saveReview(request: CreateReviewRequest): Boolean
}