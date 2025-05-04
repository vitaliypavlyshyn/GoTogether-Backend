package com.example.features.reviews.presentation

import com.example.features.locations.presentation.LocationController
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureReviewRouting() {
    routing {
        get("/reviews/reviewed/{reviewedUuid}") {
            ReviewController(call).getReviewsByReviewedUuid()
        }
        get("/reviews/reviewer/{reviewerUuid}") {
            ReviewController(call).getReviewsByReviewerUuid()
        }

        get("/ratings/{userUuid}") {
            ReviewController(call).getAvgRatingByUserUuid()
        }

        post("/review") {
            ReviewController(call).postReview()
        }
    }
}