package com.example.database.cars

import com.example.features.cars.CarResponse
import kotlinx.serialization.Serializable

@Serializable
data class CarDTO(
    val carId: Long,
    val make: String,
    val model: String
)


fun CarDTO.toCarResponse(): CarResponse {
    return CarResponse(
        carId = carId,
        make = make,
        model = model
    )
}

fun List<CarDTO>.toCarsResponse(): List<CarResponse> {
    val carsResponse = mutableListOf<CarResponse>()
    for(i in this) {
        carsResponse.add(i.toCarResponse())
    }
    return carsResponse
}