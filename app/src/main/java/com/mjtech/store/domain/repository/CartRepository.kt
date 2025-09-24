package com.mjtech.store.domain.repository

interface CartRepository {

    suspend fun addItem(productId: String)
    suspend fun removeItem(productId: String)
    suspend fun clearCart()
}