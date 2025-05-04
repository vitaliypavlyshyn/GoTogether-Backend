package com.example.utils.validator

import java.time.Duration
import java.time.Instant

object TimeValidator {
    fun isTripTimeAvailable(
        startTime: Instant,
        endTime: Instant,
        existingTrips: List<Pair<Instant, Instant>>
    ): Boolean {
        val buffer = Duration.ofHours(2)

        for ((existingStart, existingEnd) in existingTrips) {
            val rangeStart = existingStart.minus(buffer)
            val rangeEnd = existingEnd.plus(buffer)

            val overlaps = startTime < rangeEnd && endTime > rangeStart
            if (overlaps) {
                return false
            }
        }

        return true
    }
}