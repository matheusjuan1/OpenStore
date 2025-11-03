package com.mjtech.store.ui.scale

import com.mjtech.store.domain.common.Result
import com.mjtech.store.domain.scale.model.PriceSetting

data class ScaleUiState(
    val price: Result<PriceSetting> = Result.Loading,
    val weight: Result<Double> = Result.Loading,
    val initialize: Result<Unit> = Result.Loading,
    val finish: Result<Unit> = Result.Loading,
    val activate: Result<Unit> = Result.Loading,
    val deactivate: Result<Unit> = Result.Loading
)