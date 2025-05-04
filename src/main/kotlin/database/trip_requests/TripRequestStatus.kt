package com.example.database.trip_requests

enum class TripRequestStatus(val type: String) {
    PENDING("Очікується"),
    ACCEPTED("Підтверджено"),
    DECLINED("Відхилено"),
    CANCELED("Скасовано")
}