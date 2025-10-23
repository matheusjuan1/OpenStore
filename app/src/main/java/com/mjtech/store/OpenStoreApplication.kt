package com.mjtech.store

import android.app.Application
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.request.crossfade
import com.mjtech.store.di.storeModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class OpenStoreApplication : Application(), SingletonImageLoader.Factory {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@OpenStoreApplication)
            modules(storeModules)
        }
    }

    override fun newImageLoader(context: android.content.Context): ImageLoader {
        return ImageLoader.Builder(context)
            .crossfade(true)
            .build()
    }
}