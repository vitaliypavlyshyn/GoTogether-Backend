package com.example.features.locations.repository

import com.example.database.cities.CityDTO
import com.example.database.regions.RegionDTO

interface LocationRepository {
    fun findAllCities(): List<CityDTO>
    fun findCityById(id: Long): CityDTO?
    fun findAllRegions(): List<RegionDTO>
    fun funRegionById(id: Long): RegionDTO?
}