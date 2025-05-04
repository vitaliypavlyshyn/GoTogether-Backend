package com.example.features.reviews.presentation

import com.example.database.reviews.toDetailedReviewsResponse
import com.example.database.reviews.toRatingResponse
import com.example.database.reviews.toReviewsResponse
import com.example.database.users.UserDTO
import com.example.features.reviews.CreateReviewRequest
import com.example.features.reviews.domain.ReviewService
import com.example.features.users.UpdateUserRequest
import com.example.features.users.UserResponse
import com.example.features.users.domain.UserService
import com.example.utils.Controller
import com.example.utils.extensions.capitalizeName
import com.example.utils.parser.ParserDate
import com.example.utils.validator.UserValidator
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class ReviewController(private val call: ApplicationCall): Controller() {
    suspend fun getReviewsByReviewedUuid() {
        val user = checkToken(call)

        if(user == null) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid or expired token")
        } else {
            val reviewedUuid = call.parameters["reviewedUuid"]

            if (reviewedUuid == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid user UUID")
                return
            }

            val reviews = ReviewService.fetchReviewsByReviewedUuid(reviewedUuid)?.toReviewsResponse()
            val reviewers  = mutableListOf<UserResponse>()
            if(reviews != null) {
                for (i in reviews) {
                    reviewers.add(UserService.fetchUserByUuid(i.reviewerUuid)!!)
                }
            }
            val detailedReviews = ReviewService.fetchReviewsByReviewedUuid(reviewedUuid)?.toDetailedReviewsResponse(reviewers)
            if(detailedReviews != null) {
                call.respond(HttpStatusCode.OK, detailedReviews)
            } else {
                call.respond(HttpStatusCode.BadRequest, "User not exist")
            }
        }
    }


    suspend fun getReviewsByReviewerUuid() {
        val user = checkToken(call)

        if(user == null) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid or expired token")
        } else {
            val reviewerUuid = call.parameters["reviewerUuid"]

            if (reviewerUuid == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid user UUID")
                return
            }

            val reviews = ReviewService.fetchReviewsByReviewerUuid(reviewerUuid)?.toReviewsResponse()
            val reviewers  = mutableListOf<UserResponse>()
            if(reviews != null) {
                for (i in reviews) {
                    reviewers.add(UserService.fetchUserByUuid(i.reviewerUuid)!!)
                }
            }
            val detailedReviews = ReviewService.fetchReviewsByReviewerUuid(reviewerUuid)?.toDetailedReviewsResponse(reviewers)
            if(detailedReviews != null) {
                call.respond(HttpStatusCode.OK, detailedReviews)
            } else {
                call.respond(HttpStatusCode.BadRequest, "User not exist")
            }
        }
    }

    suspend fun getAvgRatingByUserUuid() {
        val user = checkToken(call)

        if(user == null) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid or expired token")
        } else {
            val uuid = call.parameters["userUuid"]

            if (uuid == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid user UUID")
                return
            }

            val reviews = ReviewService.fetchReviewsByReviewedUuid(uuid)?.toRatingResponse()
            if(reviews != null) {
                call.respond(HttpStatusCode.OK, reviews)
            } else {
                call.respond(HttpStatusCode.BadRequest, "User not exist")
            }
        }
    }


    suspend fun postReview() {
        val user = checkToken(call)

        if (user == null) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid or expired token")
            return
        }
        val request = call.receive<CreateReviewRequest>()
        val response = ReviewService.saveReview(request)

        call.respond(HttpStatusCode.OK, response)

    }
}