package com.example.database.trips

enum class TripStatus(val type: String) {
    SCHEDULED("Заплановано"),
    IN_PROGRESS("Відбувається"),
    COMPLETED("Завершено"),
    CANCELED("Скасовано")
}