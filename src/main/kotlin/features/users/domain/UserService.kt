package com.example.features.users.domain

import com.example.database.auth.Auth
import com.example.database.cars.CarDTO
import com.example.database.reviews.toRatingResponse
import com.example.database.users.toUserResponse
import com.example.features.cars.domain.CarService
import com.example.features.login.repository.LoginRepositoryImpl
import com.example.features.reviews.domain.ReviewService
import com.example.features.trip_requests.Response
import com.example.features.users.UpdateUserRequest
import com.example.features.users.UserResponse
import com.example.features.users.repository.UserRepositoryImpl
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object UserService {
    fun fetchUserByToken(token: String): UserResponse? {
        val userUuid = transaction {
            Auth.select { Auth.userUuid eq token }
                .singleOrNull()
                ?.get(Auth.userUuid)
        } ?: return null

        val user = UserRepositoryImpl.findByUuid(userUuid) ?: return null
        val ratingResponse = ReviewService.fetchReviewsByReviewedUuid(userUuid)?.toRatingResponse()
        val authDTO = LoginRepositoryImpl.findAuthByUserUid(user.userUuid) ?: return null
        var carDTO: CarDTO? = null
        if(user.carId != null) {
            carDTO = CarService.fetchCarById(user.carId)
        }
        return if (ratingResponse != null) {
            user.toUserResponse(ratingResponse, carDTO, authDTO)
        } else null
    }

    fun fetchUserByUuid(userUuid: String): UserResponse? {
        val user = UserRepositoryImpl.findByUuid(userUuid) ?: return null
        val ratingResponse = ReviewService.fetchReviewsByReviewedUuid(userUuid)?.toRatingResponse()
        var carDTO: CarDTO? = null
        val authDTO = LoginRepositoryImpl.findAuthByUserUid(user.userUuid)
        if(user.carId != null) {
            carDTO = CarService.fetchCarById(user.carId)
        }
        return if (ratingResponse != null) {
            user.toUserResponse(ratingResponse, carDTO, authDTO)
        } else null
    }

    fun fetchAllUsers(): List<UserResponse> {
        val users = UserRepositoryImpl.findAll()
        val response = mutableListOf<UserResponse>()
        for (user in users) {
            val ratingResposne = ReviewService.fetchReviewsByReviewedUuid(user.userUuid)?.toRatingResponse()
            var carDTO: CarDTO? = null
            val authDTO = LoginRepositoryImpl.findAuthByUserUid(user.userUuid)
            if(user.carId != null) {
                carDTO = CarService.fetchCarById(user.carId)
            }
            if (ratingResposne != null) {
                response.add(user.toUserResponse(ratingResposne, carDTO, authDTO))
            }
        }
        return response
    }

    fun updateUser(userUuid: String, request: UpdateUserRequest): Response {
        return if(UserRepositoryImpl.updateUser(userUuid, request)) {
            Response(
                isSuccess = true,
                message = "Користувача оновлено успішно"
            )
        } else {
            Response(
                isSuccess = false,
                message = "Помилка оновлення користувача"
            )
        }
    }

    fun deleteUser(userUuid: String): Response {
        if(UserRepositoryImpl.deleteUser(userUuid)) {
            return Response(
                isSuccess = true,
                message = "Обліковий запис видалено успішно"
            )
        }
        return Response(
            isSuccess = false,
            message = "Помилка видалення облікового запису"
        )
    }
}