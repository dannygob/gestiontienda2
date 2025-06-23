package com.example.gestiontienda2.data.local.database


import android.content.Context
import androidx.room.Room
import com.example.gestiontienda2.data.local.dao.ClientDao
import com.example.gestiontienda2.data.local.dao.OrderDao
import com.example.gestiontienda2.data.local.dao.ProductDao
import com.example.gestiontienda2.data.local.dao.ProviderDao
import com.example.gestiontienda2.data.local.dao.PurchaseDao
import com.example.gestiontienda2.data.local.dao.SaleDao
import com.example.gestiontienda2.data.local.dao.ServiceExpenseDao
import com.example.gestiontienda2.data.local.dao.StockAdjustmentDao
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
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "gestion_tienda_database"
        ).build()
    }

    @Provides
    fun provideClientDao(db: AppDatabase): ClientDao = db.clientDao()
    @Provides
    fun provideProductDao(db: AppDatabase): ProductDao = db.productDao()
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
    @Provides
    fun provideStockAdjustmentDao(db: AppDatabase): StockAdjustmentDao = db.stockAdjustmentDao()
}
