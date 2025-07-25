package com.example.gestiontienda2.di

import android.content.Context
import androidx.room.Room
import com.example.gestiontienda2.data.local.dao.ClientDao
import com.example.gestiontienda2.data.local.dao.OrderDao
import com.example.gestiontienda2.data.local.dao.ProductDao
import com.example.gestiontienda2.data.local.dao.ProviderDao
import com.example.gestiontienda2.data.local.dao.PurchaseDao
import com.example.gestiontienda2.data.local.dao.SaleDao
import com.example.gestiontienda2.data.local.dao.ServiceExpenseDao
import com.example.gestiontienda2.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    fun provideProductDao(db: AppDatabase): ProductDao = db.productDao()
    @Provides
    fun provideClientDao(db: AppDatabase): ClientDao = db.clientDao()
    @Provides
    fun provideSaleDao(db: AppDatabase): SaleDao = db.saleDao()
    @Provides
    fun providePurchaseDao(db: AppDatabase): PurchaseDao = db.purchaseDao()
    @Provides
    fun provideProviderDao(db: AppDatabase): ProviderDao = db.providerDao()
    @Provides
    fun provideOrderDao(db: AppDatabase): OrderDao = db.orderDao()
    @Provides
    fun provideServiceExpenseDao(db: AppDatabase): ServiceExpenseDao = db.serviceExpenseDao()
}
