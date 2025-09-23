package br.com.projetoacbr.food.ui.products

import br.com.projetoacbr.food.domain.common.DataResult
import br.com.projetoacbr.food.domain.model.Category
import br.com.projetoacbr.food.domain.model.Product

data class ProductsUiState(
    val categories: DataResult<List<Category>> = DataResult.Loading,
    val products: DataResult<List<Product>> = DataResult.Loading,
    val categorySelected: Int = 0,
)
