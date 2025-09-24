package com.mjtech.store.ui.products

import com.mjtech.store.domain.common.DataResult
import com.mjtech.store.domain.model.Category
import com.mjtech.store.domain.model.Product

data class ProductsUiState(
    val categories: DataResult<List<Category>> = DataResult.Loading,
    val products: DataResult<List<Product>> = DataResult.Loading,
    val categorySelected: Int = 0,
)
