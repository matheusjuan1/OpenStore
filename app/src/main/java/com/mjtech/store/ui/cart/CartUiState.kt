package com.mjtech.store.ui.cart

import com.mjtech.store.domain.common.DataResult

data class CartUiState(
    val totalItemsCount: Int = 0,
    val totalPrice: Double = 0.0,
    val quantitiesProducts: Map<String, Int> = emptyMap(),
    val addItemState: DataResult<Unit> = DataResult.Success(Unit),
    val removeItemState: DataResult<Unit> = DataResult.Success(Unit)
)