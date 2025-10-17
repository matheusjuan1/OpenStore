package com.mjtech.store.ui.products.state

import com.mjtech.store.domain.common.DataResult

data class CartUiState(
    val addItemState: DataResult<Unit> = DataResult.Success(Unit),
    val removeItemState: DataResult<Unit> = DataResult.Success(Unit)
)