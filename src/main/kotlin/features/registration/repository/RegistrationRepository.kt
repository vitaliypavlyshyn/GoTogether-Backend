package com.example.features.registration.repository

interface RegistrationRepository {
    fun createNewAccount(authUuid: String,
                         userUuid: String,
                         email: String,
                         password: String,
                         firstName: String,
                         lastName: String,
                         dateOfBirth: String)
    //fun createNewUser(userUuid: String, firstName: String, lastName: String, dateOfBirth: String)
}