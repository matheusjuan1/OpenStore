package com.mjtech.store

import android.app.Application
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.request.crossfade
import com.mjtech.acbrlib.bal.di.balModule
import com.mjtech.store.di.storeModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.io.File

class OpenStoreApplication : Application(), SingletonImageLoader.Factory {

    private lateinit var appDir: String

    override fun onCreate() {
        super.onCreate()
        val externalDir = getExternalFilesDir(null)
        appDir = if (externalDir != null) externalDir.absolutePath else ""

        startKoin {
            androidContext(this@OpenStoreApplication)
            modules(
                storeModules,
                balModule(appDir)
            )
        }
    }

    override fun newImageLoader(context: android.content.Context): ImageLoader {
        return ImageLoader.Builder(context)
            .crossfade(true)
            .build()
    }
}