package com.fclarke.animals.model

import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface AnimalApi {

    @GET("getKey")  //get the key for the POST
    fun getApiKey(): Single<ApiKey> //observable single value or error

    @FormUrlEncoded //form URL encoding. Fields should be declared as parameters and annotated with @Field.
    @POST("getAnimals")
    fun getAnimals(@Field("key") key: String): Single<List<Animal>>
}