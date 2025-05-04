package com.example.database.cities

import com.example.database.regions.RegionDTO
import com.example.features.locations.LocationResponse


data class CityDTO (
    val cityId: Long,
    val regionId: Long,
    val cityName: String,
    val cityNameUk: String,
    val lat: String,
    val lng: String,
    val country: String,
    val iso2: String,
)

fun CityDTO.toLocationResponse(regionDTO: RegionDTO): LocationResponse {
    return LocationResponse(
        cityId = this.cityId,
        regionId = this.regionId,
        cityName = this.cityName,
        cityNameUk = this.cityNameUk,
        lat = this.lat,
        lng = this.lng,
        country = this.country,
        iso2 = this.iso2,
        adminName = regionDTO.adminName,
        adminNameUk = regionDTO.adminNameUk,
    )
}