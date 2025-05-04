package com.example.database.cities

import com.example.database.regions.Regions
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Cities: Table("cities") {
    val cityId = long("city_id").autoIncrement()
    val regionId = long("region_id")
        .references(Regions.regionId, onDelete = ReferenceOption.CASCADE)
    val cityName = varchar("city", 60)
    val cityNameUk = varchar("city_uk", 60)
    val lat = varchar("lat", 15)
    val lng = varchar("lng", 15)
    val country = varchar("country", 60)
    val iso2 = varchar("iso2", 3)

    override val primaryKey = PrimaryKey(cityId)

}