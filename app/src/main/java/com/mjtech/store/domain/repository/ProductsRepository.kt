package com.mjtech.store.domain.repository

import com.mjtech.store.domain.common.DataResult
import com.mjtech.store.domain.model.Category
import com.mjtech.store.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {

    suspend fun getCategories(): Flow<DataResult<List<Category>>>
    suspend fun getProducts(): Flow<DataResult<List<Product>>>
}