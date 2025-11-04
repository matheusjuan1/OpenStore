package com.mjtech.store.domain.scale.repository

import com.mjtech.store.domain.common.Result
import com.mjtech.store.domain.scale.model.PriceSetting
import kotlinx.coroutines.flow.Flow

interface PricingRepository {

    fun getPriceSetting(): Flow<Result<PriceSetting>>
}