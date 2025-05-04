package com.example.database.users

import org.jetbrains.exposed.sql.ResultRow

fun mapToUserDTO(row: ResultRow): UserDTO {
    return UserDTO(
        userUuid = row[Users.userUuid],
        carId = row[Users.carId],
        pictureProfile = row[Users.pictureProfile],
        firstName = row[Users.firstName],
        lastName = row[Users.lastName],
        dateOfBirth = row[Users.dateOfBirth].toString(),
        phoneNumber = row[Users.phoneNumber],
        description = row[Users.description],
        isDeleted = row[Users.isDeleted]
    )
}