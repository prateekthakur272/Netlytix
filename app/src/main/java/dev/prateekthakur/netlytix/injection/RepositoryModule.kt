package dev.prateekthakur.netlytix.injection

import dev.prateekthakur.netlytix.data.repository.MobileConnectionRepositoryImpl
import dev.prateekthakur.netlytix.data.repository.NetworkConnectionRepositoryImpl
import dev.prateekthakur.netlytix.data.repository.WifiConnectionRepositoryImpl
import dev.prateekthakur.netlytix.domain.repository.MobileNetworkConnectionRepository
import dev.prateekthakur.netlytix.domain.repository.NetworkConnectionRepository
import dev.prateekthakur.netlytix.domain.repository.WifiConnectionRepository
import org.koin.dsl.module

val RepositoryModule = module{
    single<NetworkConnectionRepository> {
        NetworkConnectionRepositoryImpl(get())
    }

    single<WifiConnectionRepository> {
        WifiConnectionRepositoryImpl(get())
    }

    single<MobileNetworkConnectionRepository> {
        MobileConnectionRepositoryImpl(get())
    }
}