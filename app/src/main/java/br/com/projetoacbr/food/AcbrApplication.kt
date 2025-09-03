package br.com.projetoacbr.food

import android.app.Application
import br.com.projetoacbr.food.di.acbrFoodModules
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