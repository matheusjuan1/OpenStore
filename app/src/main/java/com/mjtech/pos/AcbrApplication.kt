package com.mjtech.pos

import android.app.Application
import com.mjtech.pos.di.acbrFoodModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AcbrApplication : Application() {

    companion object {
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AcbrApplication)
            modules(acbrFoodModules)
        }
    }
}