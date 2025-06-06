package com.example.gestiontienda2.di

import com.example.gestiontienda2.data.remote.api.OpenFoodFactsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://world.openfoodfacts.org/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideOpenFoodFactsApiService(retrofit: Retrofit): OpenFoodFactsApiService =
        retrofit.create(OpenFoodFactsApiService::class.java)
}

