package com.fclarke.animals.di.components

import com.fclarke.animals.di.modules.ApiModule
import com.fclarke.animals.model.AnimalApiService
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent { //=>DaggerApiComponent

    fun inject(service: AnimalApiService) //used in AnimalApiService
}