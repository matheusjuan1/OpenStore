package com.mjtech.acbrlib.bal.data.repository

import com.mjtech.store.domain.common.Result
import kotlinx.coroutines.flow.Flow

interface BalConfigRepository {

    fun inicializar(): Flow<Result<Unit>>

    fun finalizar(): Flow<Result<Unit>>

    fun ativar(): Flow<Result<Unit>>

    fun desativar(): Flow<Result<Unit>>
}