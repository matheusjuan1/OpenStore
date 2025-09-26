package com.mjtech.store.data.local.model

import com.mjtech.store.domain.model.Product
import kotlinx.serialization.Serializable

@Serializable
data class ProductsDto(
    val products: List<Product>
)
