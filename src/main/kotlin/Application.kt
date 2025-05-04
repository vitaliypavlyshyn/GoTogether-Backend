package com.example


import com.example.features.activities_log.presentation.configureActivityLogRouting
import com.example.features.login.presentation.configureLoginRouting
import com.example.features.cars.presentation.configureCarRouting
import com.example.features.locations.presentation.configureLocationRouting
import com.example.features.registration.presentation.configureRegisterRouting
import com.example.features.reviews.presentation.configureReviewRouting
import com.example.features.trip_passengers.presentation.configureTripPassengerRouting
import com.example.features.trip_requests.presentation.configureTripRequestRouting
import com.example.features.trips.presentation.configureTripRouting
import com.example.features.users.presentation.configureUserInfoRouting
import com.example.plugins.configureRouting
import com.example.plugins.configureSerialization
import com.example.utils.scheduler.TripExpirationScheduler

import io.ktor.server.application.*

fun main(args: Array<String>) {
    connectToDatabase()

    io.ktor.server.cio.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
    configureLoginRouting()
    configureUserInfoRouting()
    configureCarRouting()
    configureLocationRouting()
    configureReviewRouting()
    configureTripRouting()
    configureTripRequestRouting()
    configureTripPassengerRouting()
    configureActivityLogRouting()
    configureRegisterRouting()
    TripExpirationScheduler.init(this)
}

