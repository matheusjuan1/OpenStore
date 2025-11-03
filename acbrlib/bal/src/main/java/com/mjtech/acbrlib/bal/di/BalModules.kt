package com.mjtech.acbrlib.bal.di

import com.mjtech.acbrlib.bal.data.repository.BalConfigRepository
import com.mjtech.acbrlib.bal.data.repository.BalRepository
import com.mjtech.acbrlib.bal.data.source.ACBrLibBALManager
import com.mjtech.store.domain.scale.repository.ScaleRepository
import org.koin.dsl.module

fun balModule(appDir: String) = module {

    single<ACBrLibBALManager> { ACBrLibBALManager.getInstance(appDir) }

    single<BalConfigRepository> { BalRepository(acbrLibBal = get()) }

    single<ScaleRepository> { BalRepository(acbrLibBal = get()) }
}