package com.mjtech.store.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: Int,
    val name: String
)
