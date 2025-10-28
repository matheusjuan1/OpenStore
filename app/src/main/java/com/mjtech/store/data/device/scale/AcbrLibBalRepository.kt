package com.mjtech.store.data.device.scale

import com.mjtech.store.domain.common.Result
import com.mjtech.store.domain.scale.repository.ScaleRepository
import kotlinx.coroutines.flow.flow

class AcbrLibBalRepository(private val acbrLibBal: Double): ScaleRepository {

    override fun getWeight() = flow {
        emit(Result.Loading)
        try {
            val weight = acbrLibBal
            emit(Result.Success(weight))
        } catch (e: Exception) {
            emit(Result.Error("Erro ao ao obter o peso: ${e.message}"))
        }
    }
}