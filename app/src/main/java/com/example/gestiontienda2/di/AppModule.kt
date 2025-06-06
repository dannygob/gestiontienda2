package com.example.gestiontienda2.di

import android.content.Context
import androidx.room.Room
import com.example.gestiontienda2.data.local.dao.ProductDao
import com.example.gestiontienda2.data.local.database.AppDatabase
import com.example.gestiontienda2.data.remote.api.OpenFoodFactsApiService
import com.example.gestiontienda2.data.repository.ClientRepositoryImpl
import com.example.gestiontienda2.data.repository.OrderRepositoryImpl
import com.example.gestiontienda2.data.repository.ProductRepositoryImpl
import com.example.gestiontienda2.data.repository.ProviderRepositoryImpl
import com.example.gestiontienda2.data.repository.PurchaseRepositoryImpl
import com.example.gestiontienda2.data.repository.SaleRepositoryImpl
import com.example.gestiontienda2.data.repository.ServiceExpenseRepositoryImpl
import com.example.gestiontienda2.domain.repository.ClientRepository
import com.example.gestiontienda2.domain.repository.OrderRepository
import com.example.gestiontienda2.domain.repository.PurchaseRepository
import com.example.gestiontienda2.domain.repository.ServiceExpenseRepository
import com.gestiontienda2.domain.repository.ProductRepository
import com.gestiontienda2.domain.repository.ProviderRepository
import com.gestiontienda2.domain.repository.SaleRepository
import dagger.Binds
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

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideProductDao(database: AppDatabase): ProductDao = database.productDao()

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://world.openfoodfacts.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOpenFoodFactsApiService(retrofit: Retrofit): OpenFoodFactsApiService {
        return retrofit.create(OpenFoodFactsApiService::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository

    @Binds
    abstract fun bindClientRepository(
        clientRepositoryImpl: ClientRepositoryImpl
    ): ClientRepository

    @Binds
    abstract fun bindSaleRepository(
        saleRepositoryImpl: SaleRepositoryImpl
    ): SaleRepository

    @Binds
    abstract fun bindPurchaseRepository(
        purchaseRepositoryImpl: PurchaseRepositoryImpl
    ): PurchaseRepository

    @Binds
    abstract fun bindProviderRepository(
        providerRepositoryImpl: ProviderRepositoryImpl
    ): ProviderRepository

    @Binds
    abstract fun bindOrderRepository(
        orderRepositoryImpl: OrderRepositoryImpl
    ): OrderRepository

    @Binds
    abstract fun bindServiceExpenseRepository(
        serviceExpenseRepositoryImpl: ServiceExpenseRepositoryImpl
    ): ServiceExpenseRepository
}
