package com.example.database.users

import com.example.database.cars.Cars
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.date

object Users : Table("users") {
    val userUuid = varchar("user_uuid", 50)
    val carId = long("car_id").references(Cars.carId).nullable()
    val pictureProfile = binary("picture_profile").nullable()
    val firstName = varchar("first_name", 30)
    val lastName = varchar("last_name", 30)
    val dateOfBirth = date("date_of_birth")
    val phoneNumber = varchar("phone_number", 10).nullable()
    val description = varchar("description", 150).nullable()
    val isDeleted = bool("is_deleted").default(false)

    override val primaryKey = PrimaryKey(userUuid)

}