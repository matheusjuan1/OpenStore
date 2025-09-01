package br.com.projetoacbr.food.domain.repository

import br.com.projetoacbr.food.domain.common.DataResult
import br.com.projetoacbr.food.domain.model.Category
import br.com.projetoacbr.food.domain.model.Product

interface ProductsRepository {

    suspend fun getCategories(): DataResult<List<Category>>
    suspend fun getProducts(): DataResult<List<Product>>
}