package com.your_app_name.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.your_app_name.data.local.dao.ProductDao
import com.your_app_name.data.local.dao.SaleDao
import com.your_app_name.data.local.dao.PurchaseDao
import com.your_app_name.data.local.dao.ClientDao
import com.your_app_name.data.local.dao.OrderDao
import com.your_app_name.data.local.dao.ProviderDao
import com.your_app_name.data.local.dao.ServiceExpenseDao
import com.your_app_name.data.local.entities.ProductEntity
import com.your_app_name.data.local.entities.OrderEntity
import com.your_app_name.data.local.entities.PurchaseDetailEntity


import com.your_app_name.data.local.entities.SaleEntity
import com.your_app_name.data.local.entities.SaleDetailEntity
import com.your_app_name.data.local.entities.ClientEntity
import com.your_app_name.data.local.entities.ProviderEntity
import com.your_app_name.data.local.entities.PurchaseEntity
import com.your_app_name.data.local.entities.ServiceExpenseEntity


@Database(entities = [ProductEntity::class, ClientEntity::class, SaleEntity::class, SaleDetailEntity::class, PurchaseEntity::class, PurchaseDetailEntity::class, ProviderEntity::class, OrderEntity::class, ServiceExpenseEntity::class], version = 6, exportSchema = false)
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

    abstract fun clientDao(): ClientDao

    abstract fun saleDao(): SaleDao

    abstract fun purchaseDao(): PurchaseDao
    abstract fun providerDao(): ProviderDao
 abstract fun orderDao(): OrderDao
 abstract fun serviceExpenseDao(): ServiceExpenseDao
}
@Database(entities = [ProductEntity::class, ClientEntity::class, SaleEntity::class, SaleDetailEntity::class, PurchaseEntity::class, PurchaseDetailEntity::class, ProviderEntity::class], version = 5, exportSchema = false)