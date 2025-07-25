package dev.prateekthakur.netlytix.injection

import dev.prateekthakur.netlytix.ui.features.networkInfo.viewmodels.NetworkInfoViewModel
import dev.prateekthakur.netlytix.ui.features.networkInfo.viewmodels.WifiConnectionInfoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModelModule = module{
    viewModel {
        NetworkInfoViewModel(get())
    }

    viewModel {
        WifiConnectionInfoViewModel(get())
    }
}