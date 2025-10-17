package com.mjtech.store.domain.repository

import com.mjtech.store.domain.common.DataResult
import com.mjtech.store.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    fun addItem(productId: String): Flow<DataResult<Unit>>
    fun removeItem(productId: String): Flow<DataResult<Unit>>
    fun clearCart(): Flow<DataResult<Unit>>
    fun getCartItems(): Flow<DataResult<List<Product>>>
}