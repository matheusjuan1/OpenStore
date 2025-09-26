package com.mjtech.store.domain.model

import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class Product(
    val id: Int,
    val categoryId: Int,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val isAvailable: Boolean
)
