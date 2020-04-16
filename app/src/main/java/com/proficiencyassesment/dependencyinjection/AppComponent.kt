package com.proficiencyassesment.di


import com.proficiencyassesment.viewmodel.MainViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ApiClient::class])
interface AppComponent {
    fun inject(mainViewModel: MainViewModel)
}