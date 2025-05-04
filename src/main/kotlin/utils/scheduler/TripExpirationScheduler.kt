package com.example.utils.scheduler

import com.example.database.trip_requests.TripRequestStatus
import com.example.database.trip_requests.TripRequests
import com.example.database.trips.TripStatus
import com.example.database.trips.Trips
import io.ktor.server.application.*
import kotlinx.coroutines.*
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.Instant
import java.time.temporal.ChronoUnit

object TripExpirationScheduler {
    fun init(application: Application) {
        startTripStatusScheduler(application)
        startCancelLateTripRequestsJob(application)
    }

    private fun startTripStatusScheduler(application: Application) {
        val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

        application.environment.monitor.subscribe(ApplicationStopped) {
            scope.cancel()
        }

        scope.launch {
            while (isActive) {
               // delay(1800_000L)
                transaction {
                    Trips.update({ Trips.startTime less Instant.now().toKotlinInstant() }) {
                        it[status] = TripStatus.IN_PROGRESS.type
                    }
                    Trips.update({ Trips.endTime less Instant.now().toKotlinInstant() }) {
                        it[status] = TripStatus.COMPLETED.type
                    }
                }
                delay(1800_000L)
            }
        }
    }

    private fun startCancelLateTripRequestsJob(application: Application) {
        val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

        application.environment.monitor.subscribe(ApplicationStopped) {
            scope.cancel()
        }

        scope.launch {
            while (isActive) {
                delay(60_000L)

                transaction {
                    val now = Instant.now()
                    val threshold = now.plus(20, ChronoUnit.MINUTES)

                    TripRequests.innerJoin(Trips)
                        .select {
                            (TripRequests.status eq TripRequestStatus.PENDING.type) and
                                    (Trips.startTime lessEq threshold.toKotlinInstant())
                        }
                        .map { it[TripRequests.requestId] }
                        .forEach { requestId ->
                            TripRequests.update({ TripRequests.requestId eq requestId }) {
                                it[status] = TripRequestStatus.DECLINED.type
                                it[updatedAt] = Instant.now().toKotlinInstant()
                            }
                        }
                }
            }
        }
    }
}