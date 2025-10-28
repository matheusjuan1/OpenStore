package com.mjtech.acbrlib.bal.data

import com.mjtech.store.domain.common.Result
import com.mjtech.store.domain.scale.repository.ScaleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BalRepository(private val acbrLibBal: Double): ScaleRepository {

    override fun getWeight(): Flow<Result<Double>> = flow {
        emit(Result.Loading)
        try {
            val weight = acbrLibBal
            emit(Result.Success(weight))
        } catch (e: Exception) {
            emit(Result.Error("Erro ao ao obter o peso: ${e.message}"))
        }
    }
}