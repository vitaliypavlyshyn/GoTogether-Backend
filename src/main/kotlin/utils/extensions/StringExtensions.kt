package com.example.utils.extensions

fun String.capitalizeName(): String {
    return this.lowercase().replaceFirstChar { it.uppercase() }
}