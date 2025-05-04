package com.example.features.registration.repository

import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.database.auth.Auth
import com.example.database.users.Users
import com.example.utils.extensions.capitalizeName

import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import kotlinx.datetime.toLocalDate
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
object RegistrationRepositoryImpl : RegistrationRepository {
    override fun createNewAccount(
        authUuid: String,
        userUuid: String,
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        dateOfBirth: String
    ) {
        return transaction {
            Users.insert {
                it[this.userUuid] =userUuid
                it[carId] = null
                it[pictureProfile] = null
                it[this.firstName] = firstName.capitalizeName()
                it[this.lastName] = lastName.capitalizeName()
                it[this.dateOfBirth] = dateOfBirth.toLocalDate()
                it[phoneNumber] = null
                it[description] = null
            }
            Auth.insert {
                it[this.authUuid] = authUuid
                it[this.userUuid] = userUuid
                it[this.email] = email
                it[this.password] = password
                it[isBlocked] = false
                it[createdAt] = Instant.now().toKotlinInstant()
            }
        }
    }
}