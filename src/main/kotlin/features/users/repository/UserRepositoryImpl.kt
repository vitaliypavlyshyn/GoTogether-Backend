package com.example.features.users.repository

import com.example.database.auth.Auth
import com.example.database.users.UserDTO
import com.example.database.users.Users
import com.example.database.users.mapToUserDTO
import com.example.features.users.UpdateUserRequest
import com.example.features.users.applyToUpdate
import com.example.utils.extensions.capitalizeName
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object UserRepositoryImpl: UserRepository {
    override fun findByUuid(userUuid: String): UserDTO? {
        return transaction {
            Users.select { Users.userUuid eq userUuid }.map {
                mapToUserDTO(it)
            }.singleOrNull()
        }
    }

    override fun findAll(): List<UserDTO> {
        return transaction {
            Users.selectAll().map {
                mapToUserDTO(it)
            }
        }
    }

    override fun updateUser(userUuid: String, request: UpdateUserRequest): Boolean {
        return transaction {
            val updatedRows = Users.update({ Users.userUuid eq userUuid }) {
                var hasUpdate = false
                val requestCarId = if(request.carId == -1L) null else request.carId
                val requestPhoneNumber = if(request.phoneNumber == "") null else request.phoneNumber
                val requestDescription = if(request.description == "") null else request.description

                request.carId?.let { it1 -> it[carId] = requestCarId; hasUpdate = true }
                request.pictureProfile?.let { it2 -> it[pictureProfile] = it2; hasUpdate = true }
                request.firstName?.let { it3 -> it[firstName] = it3.capitalizeName(); hasUpdate = true }
                request.lastName?.let { it4 -> it[lastName] = it4.capitalizeName(); hasUpdate = true }
                request.dateOfBirth?.let { it5 -> it[dateOfBirth] = LocalDate.parse(it5); hasUpdate = true }
                request.phoneNumber?.let { it6 -> it[phoneNumber] = requestPhoneNumber; hasUpdate = true }
                request.description?.let { it7 -> it[description] = requestDescription; hasUpdate = true }
                request.isDeleted.let { it8 -> it[isDeleted] = it8; hasUpdate = true}

                if (!hasUpdate) error("Nothing to update")
            }

            updatedRows > 0
        }
    }

    override fun deleteUser(userUuid: String): Boolean {
        return transaction {
            val updatedRows = Users.update({ Users.userUuid eq userUuid }) {
                it[isDeleted] = true
                it[firstName] = "Видалений аккаунт"
                it[lastName] = ""
                it[pictureProfile] = null
            }

            val deletedRows = Auth.deleteWhere {
                Auth.userUuid eq userUuid
            }

            updatedRows > 0 || deletedRows > 0
        }
    }
}