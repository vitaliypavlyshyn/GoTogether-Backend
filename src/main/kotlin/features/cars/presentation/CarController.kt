package com.example.features.cars.presentation

import com.example.database.cars.toCarResponse
import com.example.database.cars.toCarsResponse
import com.example.features.cars.domain.CarService
import com.example.utils.Controller
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class CarController(private val call: ApplicationCall): Controller() {
    suspend fun getCars() {
        val cars = CarService.fetchCars().toCarsResponse()
        call.respond(HttpStatusCode.OK, cars)
    }

    suspend fun getCarById() {
        val idParam = call.parameters["carId"]
        val carId = idParam?.toLongOrNull()

        if (carId == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid car ID")
            return
        }

        val car = CarService.fetchCarById(carId)?.toCarResponse()
        if (car == null) {
            call.respond(HttpStatusCode.NotFound, "Car not found")
        } else {
            call.respond(HttpStatusCode.OK, car)
        }
    }
}