package br.com.projetoacbr.food.domain.repository

import br.com.projetoacbr.food.domain.common.DataResult
import br.com.projetoacbr.food.domain.model.Category
import br.com.projetoacbr.food.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {

    suspend fun getCategories(): Flow<DataResult<List<Category>>>
    suspend fun getProducts(): Flow<DataResult<List<Product>>>
}