package dev.prateekthakur.netlytix.inject

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.prateekthakur.netlytix.data.repository.NetworkRepositoryImpl
import dev.prateekthakur.netlytix.domain.repository.ConnectionRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideNetworkInfoModule(context: Application) : ConnectionRepository{
        return NetworkRepositoryImpl(context)
    }
}