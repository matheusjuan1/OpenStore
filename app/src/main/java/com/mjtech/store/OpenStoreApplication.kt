package com.mjtech.store

import android.app.Application
import com.mjtech.store.di.storeModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class OpenStoreApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@OpenStoreApplication)
            modules(storeModules)
        }
    }
}