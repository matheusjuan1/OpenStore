package com.mjtech.store.domain.scale.repository

import com.mjtech.store.domain.common.Result
import kotlinx.coroutines.flow.Flow

interface ScaleRepository {

    fun getWeight(): Flow<Result<Double>>
}