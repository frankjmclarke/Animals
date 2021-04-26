package com.fclarke.animals.di.components

import com.fclarke.animals.di.modules.PrefsModule
import com.fclarke.animals.di.modules.ApiModule
import com.fclarke.animals.di.modules.AppModule
import com.fclarke.animals.viewmodel.ListViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class, PrefsModule::class, AppModule::class])
interface ViewModelComponent { //=>DaggerViewModelComponent

    fun inject(viewModel: ListViewModel) //used in ListViewModel
}