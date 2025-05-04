package com.example.database.regions

import org.jetbrains.exposed.sql.ResultRow

fun mapToRegionDTO(row: ResultRow): RegionDTO {
    return RegionDTO(
        regionId = row[Regions.regionId],
        adminName = row[Regions.adminName],
        adminNameUk = row[Regions.adminNameUk]
    )
}