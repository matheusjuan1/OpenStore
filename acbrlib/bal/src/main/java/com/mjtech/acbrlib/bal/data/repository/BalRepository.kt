package com.mjtech.acbrlib.bal.data.repository

import com.mjtech.acbrlib.bal.data.source.ACBrLibBALManager
import com.mjtech.store.domain.common.Result
import com.mjtech.store.domain.scale.repository.ScaleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BalRepository(private val acbrLibBal: ACBrLibBALManager) : ScaleRepository,
    BalConfigRepository {

    override fun getWeight(): Flow<Result<Double>> = flow {
        emit(Result.Loading)
        try {
            val weight = acbrLibBal.getLib().lePeso(1000)
            emit(Result.Success(weight))
        } catch (e: Exception) {
            emit(Result.Error("Erro ao ao obter o peso: ${e.message}"))
        }
    }

    override fun inicializar(): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            acbrLibBal.getLib().inicializar()
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error("Erro ao ao inicializar ACBrLibBAL: ${e.message}"))
        }
    }

    override fun finalizar(): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            acbrLibBal.getLib().finalizar()
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error("Erro ao ao finalizar ACBrLibBAL: ${e.message}"))
        }
    }

    override fun ativar(): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            acbrLibBal.getLib().ativar()
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error("Erro ao ao ativar balança: ${e.message}"))
        }
    }

    override fun desativar(): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            acbrLibBal.getLib().desativar()
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error("Erro ao ao desativar balança: ${e.message}"))
        }
    }
}