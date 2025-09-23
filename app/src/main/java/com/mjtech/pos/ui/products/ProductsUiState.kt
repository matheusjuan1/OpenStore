package com.mjtech.pos.ui.products

import com.mjtech.pos.domain.common.DataResult
import com.mjtech.pos.domain.model.Category
import com.mjtech.pos.domain.model.Product

data class ProductsUiState(
    val categories: DataResult<List<Category>> = DataResult.Loading,
    val products: DataResult<List<Product>> = DataResult.Loading,
    val categorySelected: Int = 0,
)
