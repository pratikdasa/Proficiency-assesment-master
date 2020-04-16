package com.proficiencyassesment.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.proficiencyassesment.retrofit.ApiService
import com.proficiencyassesment.utils.Constants
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApiClient {

    @Provides
    @Singleton
    fun providesMyOkHttpClient(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient().newBuilder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun providesHttpLoggingInterceptor(): Interceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Provides
    @Singleton
    fun providesRetrofit(myOkHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(myOkHttpClient)
            .baseUrl(Constants.BASE_API_URL) // YOUR BASE URL OF API
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun providersApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

}