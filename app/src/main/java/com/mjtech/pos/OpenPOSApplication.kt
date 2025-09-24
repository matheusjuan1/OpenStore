package com.mjtech.pos

import android.app.Application
import com.mjtech.pos.di.posModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class OpenPOSApplication : Application() {

    companion object {
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@OpenPOSApplication)
            modules(posModules)
        }
    }
}