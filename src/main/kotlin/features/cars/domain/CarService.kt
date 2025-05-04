package com.example.features.cars.domain

import com.example.database.cars.CarDTO
import com.example.features.cars.repository.CarRepositoryImpl

object CarService {
    fun fetchCars(): List<CarDTO> {
        return CarRepositoryImpl.findAll()
    }

    fun fetchCarById(id: Long): CarDTO? {
        return CarRepositoryImpl.findById(id)
    }
}