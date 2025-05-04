package com.example

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

fun connectToDatabase() {
    val dataSource = HikariDataSource(
        HikariConfig().apply {
            jdbcUrl = "jdbc:postgresql://localhost:5432/go_together_db"
            driverClassName = "org.postgresql.Driver"
            username = "postgres"
            password = "123"

        }
    )
    Database.connect(dataSource)
}