package dev.prateekthakur.netlytix

import android.app.Application
import dev.prateekthakur.netlytix.injection.RepositoryModule
import dev.prateekthakur.netlytix.injection.ViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(RepositoryModule, ViewModelModule)
        }
    }
}