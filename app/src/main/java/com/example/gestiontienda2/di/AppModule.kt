package com.example.gestiontienda2.di

import android.content.Context
import androidx.room.Room
import com.example.gestiontienda2.data.local.dao.ProductDao
import com.example.gestiontienda2.data.local.database.AppDatabase
import com.example.gestiontienda2.data.remote.api.OpenFoodFactsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // Providers for Database, ProductDao, Retrofit, and OpenFoodFactsApiService
    // have been moved to DatabaseModule.kt and NetworkModule.kt respectively.
    // This module can be used for other general application-wide bindings if needed.
}
