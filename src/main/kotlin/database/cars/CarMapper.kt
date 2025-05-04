package com.example.database.cars


import org.jetbrains.exposed.sql.ResultRow

fun mapToCarDTO(row: ResultRow): CarDTO{
    return CarDTO(
        carId = row[Cars.carId],
        make = row[Cars.make],
        model = row[Cars.model]
    )
}