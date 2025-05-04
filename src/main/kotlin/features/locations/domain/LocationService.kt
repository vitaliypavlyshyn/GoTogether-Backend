package com.example.features.locations.domain

import com.example.database.cities.toLocationResponse
import com.example.features.locations.LocationResponse
import com.example.features.locations.repository.LocationRepositoryImpl
import org.jetbrains.exposed.sql.transactions.transaction

object LocationService {
    fun fetchLocationById(id: Long): LocationResponse? {
        return transaction {
            val cityDTO = LocationRepositoryImpl.findCityById(id) ?: return@transaction null
            val regionDTO = LocationRepositoryImpl.funRegionById(cityDTO.regionId) ?: return@transaction null

            cityDTO.toLocationResponse(regionDTO)
        }
    }

    fun fetchLocations(): List<LocationResponse> {
        val citiesDTO = LocationRepositoryImpl.findAllCities()
        val regionsDTO = LocationRepositoryImpl.findAllRegions()
        val locationsResponse = mutableListOf<LocationResponse>()
        for(cityDTO in citiesDTO){
            for(regionDTO in regionsDTO) {
                if(cityDTO.regionId == regionDTO.regionId) {
                    locationsResponse.add(
                        cityDTO.toLocationResponse(regionDTO)
                    )
                }
            }
        }
        return locationsResponse
    }
}