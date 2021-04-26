package com.fclarke.animals.model

import com.fclarke.animals.di.components.DaggerApiComponent
import io.reactivex.Single
import javax.inject.Inject

class AnimalApiService { //ListViewModel uses this

    @Inject
    lateinit var api: AnimalApi

    init {
        DaggerApiComponent.create().inject(this)//get Retrofit.Builder() from AnimalApiService
    }

    fun getApiKey(): Single<ApiKey> {
        return api.getApiKey()
    }

    fun getAnimals(key: String): Single<List<Animal>> {
        return api.getAnimals(key)
    }
}