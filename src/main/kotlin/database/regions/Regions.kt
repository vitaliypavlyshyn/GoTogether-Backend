package com.example.database.regions

import org.jetbrains.exposed.sql.Table

object Regions: Table("regions") {
    val regionId = long("region_id").autoIncrement()
    val adminName = varchar("admin_name", 60)
    val adminNameUk = varchar("admin_name_uk", 60)

    override val primaryKey = PrimaryKey(regionId)

}