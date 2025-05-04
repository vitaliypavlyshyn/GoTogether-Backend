package com.example.database.cities

import org.jetbrains.exposed.sql.ResultRow

fun mapToCityDTO(row: ResultRow): CityDTO {
    return CityDTO(
        cityId = row[Cities.cityId],
        regionId = row[Cities.regionId],
        cityName = row[Cities.cityName],
        cityNameUk = row[Cities.cityNameUk],
        lat = row[Cities.lat],
        lng = row[Cities.lng],
        country = row[Cities.country],
        iso2 = row[Cities.iso2]
    )
}

