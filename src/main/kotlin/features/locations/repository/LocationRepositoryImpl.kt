package com.example.features.locations.repository

import com.example.database.cities.Cities
import com.example.database.cities.CityDTO
import com.example.database.regions.RegionDTO
import com.example.database.regions.Regions
import com.example.database.cities.mapToCityDTO
import com.example.database.regions.mapToRegionDTO
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object LocationRepositoryImpl: LocationRepository {
    override fun findAllCities(): List<CityDTO> {
        return transaction {
            Cities.selectAll().map {
                mapToCityDTO(it)
            }
        }
    }

    override fun findCityById(id: Long): CityDTO? {
        return transaction {
            Cities.select { Cities.cityId eq id }.map {
               mapToCityDTO(it)
            }.singleOrNull()
        }
    }

    override fun findAllRegions(): List<RegionDTO> {
        return transaction {
            Regions.selectAll().map {
                mapToRegionDTO(it)
            }
        }
    }

    override fun funRegionById(id: Long): RegionDTO? {
        return transaction {
            Regions.select { Regions.regionId eq id }.map {
                mapToRegionDTO(it)
            }.singleOrNull()
        }
    }
}