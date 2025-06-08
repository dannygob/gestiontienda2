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

    // Provee una instancia de AppDatabase como Singleton
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    // Provee el ProductDao desde la instancia de AppDatabase
    @Provides
    fun provideProductDao(database: AppDatabase): ProductDao {
        return database.productDao()
    }

    // Provee una instancia de Retrofit
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://world.openfoodfacts.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Provee el servicio de OpenFoodFacts a partir de Retrofit
    @Provides
    @Singleton
    fun provideOpenFoodFactsApiService(retrofit: Retrofit): OpenFoodFactsApiService {
        return retrofit.create(OpenFoodFactsApiService::class.java)
    }
}
