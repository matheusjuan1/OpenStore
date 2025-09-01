package br.com.projetoacbr.food.model

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val image: Int? = null,
    val category: String
)
