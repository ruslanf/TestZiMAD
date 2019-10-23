package studio.bz_soft.testzimad.root

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import studio.bz_soft.testzimad.di.applicationModule
import studio.bz_soft.testzimad.di.navigationModule
import studio.bz_soft.testzimad.di.networkModule
import studio.bz_soft.testzimad.di.presenterModule

class App : Application() {

    private lateinit var instance: App

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(listOf(
                applicationModule,
                networkModule,
                presenterModule,
                navigationModule
            ))
        }
    }
}