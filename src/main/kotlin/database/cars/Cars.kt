package com.example.database.cars

import org.jetbrains.exposed.sql.Table

object Cars: Table("cars") {
    val carId = Cars.long("car_id").autoIncrement()
    val make = Cars.varchar("make", 50)
    val model = Cars.varchar("model", 50)

    override val primaryKey = PrimaryKey(carId)

}