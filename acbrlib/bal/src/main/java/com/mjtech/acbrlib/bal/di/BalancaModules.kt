package com.mjtech.acbrlib.bal.di

import com.mjtech.acbrlib.bal.data.repository.BalancaRepository
import com.mjtech.acbrlib.bal.data.repository.AcbrLibBalRepository
import com.mjtech.acbrlib.bal.data.source.ACBrLibBALManager
import org.koin.dsl.module

fun balancaModule(appDir: String) = module {

    single<ACBrLibBALManager> { ACBrLibBALManager.getInstance(appDir) }

    single<BalancaRepository> { AcbrLibBalRepository(acbrLibBal = get(), appDir = appDir) }
}