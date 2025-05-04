package com.example.database.auth

import com.example.database.reviews.ReviewDTO
import com.example.database.reviews.Reviews
import kotlinx.datetime.toJavaInstant
import org.jetbrains.exposed.sql.ResultRow

fun mapToAuthDTO(row: ResultRow): AuthDTO {
    return AuthDTO(
        authUuid = row[Auth.authUuid],
        userUuid = row[Auth.userUuid],
        email = row[Auth.email],
        password = row[Auth.password],
        isBlocked = row[Auth.isBlocked],
        createdAt = row[Auth.createdAt].toJavaInstant()
    )
}