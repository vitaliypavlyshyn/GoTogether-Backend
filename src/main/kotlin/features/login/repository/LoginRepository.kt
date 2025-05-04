package com.example.features.login.repository

import com.example.database.auth.AuthDTO

interface LoginRepository {
    fun findAuthByUserUid(userUuid: String): AuthDTO?
}