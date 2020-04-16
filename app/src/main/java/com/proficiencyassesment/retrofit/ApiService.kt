package com.proficiencyassesment.retrofit

import com.proficiencyassesment.model.Facts
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("/s/{apikey}/facts.json")
    fun getFacts(@Path("apikey") key: String): Deferred<Response<Facts>>
}