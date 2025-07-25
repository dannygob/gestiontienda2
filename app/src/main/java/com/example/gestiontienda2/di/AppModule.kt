package com.example.gestiontienda2.di


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // Providers for Database, ProductDao, Retrofit, and OpenFoodFactsApiService
    // have been moved to DatabaseModule.kt and NetworkModule.kt respectively.
    // This module can be used for other general application-wide bindings if needed.

    @Provides
    @Singleton
    @Named("IODispatcher")
    fun provideIODispatcher(): CoroutineContext = Dispatchers.IO

    // You can also provide specific dispatchers if needed, e.g.:
    // @Provides
    // @Singleton
    // @Named("DefaultDispatcher")
    // fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
}
