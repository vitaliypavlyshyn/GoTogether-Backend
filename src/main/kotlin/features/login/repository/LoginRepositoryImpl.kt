package com.example.features.login.repository

import com.example.database.auth.Auth
import com.example.database.auth.AuthDTO
import com.example.database.auth.mapToAuthDTO
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object LoginRepositoryImpl: LoginRepository {
    override fun findAuthByUserUid(userUuid: String): AuthDTO? {
        return transaction {
            Auth.select { Auth.userUuid eq userUuid }.map {
                mapToAuthDTO(it)
            }.singleOrNull()
        }
    }
}