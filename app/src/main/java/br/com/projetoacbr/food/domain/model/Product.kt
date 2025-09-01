package br.com.projetoacbr.food.domain.model

import java.math.BigDecimal

data class Product(
    val id: Int,
    val categoryId: Int,
    val name: String,
    val description: String,
    val price: BigDecimal,
    val imageUrl: String,
    val isAvailable: Boolean
)
