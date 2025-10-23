package com.mjtech.store.domain.repository

import com.mjtech.store.domain.common.DataResult
import com.mjtech.store.domain.model.CartItem
import com.mjtech.store.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    fun addItem(product: Product): Flow<DataResult<Unit>>
    fun removeItem(product: Product): Flow<DataResult<Unit>>
    fun clearCart(): Flow<DataResult<Unit>>
    fun getCartItems(): Flow<DataResult<List<CartItem>>>
    fun getTotalPrice(): Flow<DataResult<Double>>
    fun getQuantityForProduct(productId: String): Flow<DataResult<Int>>
}