package com.mjtech.pos.domain.repository

import com.mjtech.pos.domain.common.DataResult
import com.mjtech.pos.domain.model.Category
import com.mjtech.pos.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {

    suspend fun getCategories(): Flow<DataResult<List<Category>>>
    suspend fun getProducts(): Flow<DataResult<List<Product>>>
}