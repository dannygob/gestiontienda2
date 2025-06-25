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
import com.your_app_name.data.local.entities.ProductEntity // Added import
import com.your_app_name.data.local.entities.OrderEntity
import com.your_app_name.data.local.entities.PurchaseDetailEntity


import com.your_app_name.data.local.entities.SaleEntity
import com.your_app_name.data.local.entities.SaleDetailEntity
import com.your_app_name.data.local.entities.ClientEntity
import com.your_app_name.data.local.entities.ProviderEntity
import com.your_app_name.data.local.entities.PurchaseEntity
import com.your_app_name.data.local.entities.ServiceExpenseEntity

import com.your_app_name.data.local.database.migrations.MIGRATION_6_7 // Assuming the new version is 7 and old is 6
@Database(entities = [ProductEntity::class, ClientEntity::class, SaleEntity::class, SaleDetailEntity::class, PurchaseEntity::class, PurchaseDetailEntity::class, ProviderEntity::class, OrderEntity::class, ServiceExpenseEntity::class], version = 7, exportSchema = false) // Incremented version to 7

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
 .addMigrations(MIGRATION_6_7) // Added the migration object - Replace MIGRATION_6_7 with your actual migration object name
 .build()
                INSTANCE = instance
                instance
            }
        }
    }

    abstract fun clientDao(): ClientDao

    abstract fun saleDao(): SaleDao

    abstract fun purchaseDao(): PurchaseDao
abstract fun providerDao(): ProviderDao
abstract fun productDao(): ProductDao
 abstract fun orderDao(): OrderDao
 abstract fun serviceExpenseDao(): ServiceExpenseDao
}
