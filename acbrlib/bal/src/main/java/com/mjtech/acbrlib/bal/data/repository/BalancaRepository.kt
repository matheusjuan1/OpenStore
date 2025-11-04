package com.mjtech.acbrlib.bal.data.repository

import com.mjtech.store.domain.common.Result
import kotlinx.coroutines.flow.Flow

interface BalancaRepository {

    fun lerPeso(): Flow<Result<Double>>

    fun inicializar(): Flow<Result<Unit>>

    fun finalizar(): Flow<Result<Unit>>

    fun ativar(): Flow<Result<Unit>>

    fun desativar(): Flow<Result<Unit>>

    fun configurarBalanca(): Flow<Result<Unit>>
}