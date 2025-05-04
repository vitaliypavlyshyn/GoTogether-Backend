package com.example.features.cars.repository

import com.example.database.cars.CarDTO
import com.example.database.cars.Cars
import com.example.database.cars.Cars.carId
import com.example.database.cars.mapToCarDTO
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object CarRepositoryImpl : CarRepository {
    override fun findById(id: Long): CarDTO? {
        return transaction {
            Cars.select { carId eq id }.map {
                mapToCarDTO(it)
            }.singleOrNull()
        }

    }

    override fun findAll(): List<CarDTO> {
        return transaction {
            Cars.selectAll().map {
                mapToCarDTO(it)
            }
        }
    }
}