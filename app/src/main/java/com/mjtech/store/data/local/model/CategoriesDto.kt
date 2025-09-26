package com.mjtech.store.data.local.model

import com.mjtech.store.domain.model.Category
import kotlinx.serialization.Serializable

@Serializable
data class CategoriesDto(
    val categories: List<Category>
)