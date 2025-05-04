package com.example.features.cars.repository

import com.example.database.cars.CarDTO

interface CarRepository {
    fun findById(id: Long): CarDTO?
    fun findAll(): List<CarDTO>
}