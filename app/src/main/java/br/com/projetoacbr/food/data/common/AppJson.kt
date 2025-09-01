package br.com.projetoacbr.food.data.common

import kotlinx.serialization.json.Json

val AppJson = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
    isLenient = true
    explicitNulls = false
}