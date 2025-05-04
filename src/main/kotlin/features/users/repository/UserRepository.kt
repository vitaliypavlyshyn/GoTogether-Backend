package com.example.features.users.repository

import com.example.database.users.UserDTO
import com.example.features.users.UpdateUserRequest

interface UserRepository {
    fun findByUuid(userUuid: String): UserDTO?
    fun findAll(): List<UserDTO>
    fun updateUser(userUuid: String, request: UpdateUserRequest): Boolean
    fun deleteUser(userUuid: String): Boolean
}