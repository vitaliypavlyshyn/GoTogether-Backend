package com.example.utils.validator

object LoginValidator {

    fun validateLogin(email: String, password: String): List<String> {
        val errors = mutableListOf<String>()

        if (!email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))) {
            errors.add("Некоректний формат пошти")
        }
        if (email.length > 30) {
            errors.add("Пошта повинна містити не більше 30 символів")
        }

        if (password.length !in 8..15) {
            errors.add("Пароль повинен містити від 8 до 15 символів")
        }
        if (!password.matches(Regex("^[A-Za-z\\d!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+$"))) {
            errors.add("Пароль повинен містити лише латинські літери, цифри та спецсимволи (без пробілів)")
        }
        if (!password.any { it.isDigit() }) {
            errors.add("Пароль повинен містити хоча б одну цифру")
        }

        return errors
    }
}