package com.example.utils.parser

import java.time.LocalDate
import java.time.format.DateTimeParseException

object ParserDate {
    fun safeParseDate(date: String?): LocalDate? {
        return try {
            date?.let { LocalDate.parse(it) }
        } catch (e: DateTimeParseException) {
            null
        }
    }
}