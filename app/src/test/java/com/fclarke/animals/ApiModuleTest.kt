package com.fclarke.animals

import com.fclarke.animals.di.modules.ApiModule
import com.fclarke.animals.model.AnimalApiService

class ApiModuleTest(val mockService: AnimalApiService): ApiModule() {
    override fun provideAnimalApiService(): AnimalApiService {
        return mockService
    }
}