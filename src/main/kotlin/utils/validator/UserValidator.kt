package com.example.utils.validator

import java.time.LocalDate
import java.time.Period

object UserValidator {
    private val validPhonePrefixes = listOf(
        "067", "068", "096", "097", "098", "077",
        "050", "066", "095", "099", "075",
        "063", "073", "093",
        "089", "094", "020"
    )

    private val nameRegex = Regex("^[А-ЯІЇЄа-яіїєA-Za-z]{2,}$")

    fun validateUserInput(
        phoneNumber: String?,
        description: String?,
        firstName: String?,
        lastName: String?,
        dateOfBirth: LocalDate?,
    ): List<String> {
        val errors = mutableListOf<String>()

        if (!phoneNumber.isNullOrBlank()) {
            if (phoneNumber.length != 10) {
                errors.add("Номер телефону повинен містити 10 цифр.")
            } else if (validPhonePrefixes.none { phoneNumber.startsWith(it) }) {
                errors.add("Номер телефону повинен починатися з коректного коду оператора.")
            } else if (!phoneNumber.all { it.isDigit() }) {
                errors.add("Номер телефону повинен містити лише цифри.")
            }
        }

        if (!description.isNullOrBlank()) {
            if (description.length !in 2..150) {
                errors.add("Опис має бути від 2 до 150 символів.")
            }
        }
        if (firstName != null) {
            if (!nameRegex.matches(firstName)) {
                errors.add("Ім’я повинно містити лише літери.")
            }
            if (firstName.length !in 2..30) {
                errors.add("Ім’я повинно містити від 2 до 30 символів.")
            }
        }
        if (lastName != null) {
            if (!nameRegex.matches(lastName)) {
                errors.add("Прізвище повинно містити лише літери.")
            }
            if (lastName.length !in 2..30) {
                errors.add("Прізвище повинно містити від 2 до 30 символів.")
            }
        }
        if(dateOfBirth != null) {
            val today = LocalDate.now()
            val age = Period.between(dateOfBirth, today).years
            if (age < 18) {
                errors.add("Користувачу має бути щонайменше 18 років.")
            }
        }
        return errors
    }
}