package com.example.features.locations.presentation

import com.example.features.locations.domain.LocationService
import com.example.utils.Controller
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class LocationController(private val call: ApplicationCall): Controller() {
    suspend fun getLocations() {
        val locations = LocationService.fetchLocations()
        call.respond(HttpStatusCode.OK, locations)
    }

    suspend fun getLocationById() {
        val idParam = call.parameters["id"]
        val id = idParam?.toLongOrNull()

        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid location ID")
            return
        }

        val location = LocationService.fetchLocationById(id)
        if (location == null) {
            call.respond(HttpStatusCode.NotFound, "Location not found")
        } else {
            call.respond(HttpStatusCode.OK, location)
        }
    }
}