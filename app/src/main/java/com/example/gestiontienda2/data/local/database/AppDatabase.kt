package com.example.gestiontienda2.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gestiontienda2.data.local.dao.ClientDao
import com.example.gestiontienda2.data.local.dao.OrderDao
import com.example.gestiontienda2.data.local.dao.ProductDao
import com.example.gestiontienda2.data.local.dao.ProviderDao
import com.example.gestiontienda2.data.local.dao.PurchaseDao
import com.example.gestiontienda2.data.local.dao.SaleDao
import com.example.gestiontienda2.data.local.dao.ServiceExpenseDao
import com.example.gestiontienda2.data.local.room.entities.ClientEntity
import com.example.gestiontienda2.data.local.room.entities.OrderEntity
import com.example.gestiontienda2.data.local.room.entities.ProductEntity
import com.example.gestiontienda2.data.local.room.entities.ProviderEntity
import com.example.gestiontienda2.data.local.room.entities.PurchaseDetailEntity
import com.example.gestiontienda2.data.local.room.entities.PurchaseEntity
import com.example.gestiontienda2.data.local.room.entities.SaleDetailEntity
import com.example.gestiontienda2.data.local.room.entities.SaleEntity
import com.example.gestiontienda2.data.local.room.entities.ServiceExpenseEntity

@Database(
    entities = [
        ProductEntity::class,
        ClientEntity::class,
        SaleEntity::class,
        SaleDetailEntity::class,
        PurchaseEntity::class,
        PurchaseDetailEntity::class,
        ProviderEntity::class,
        OrderEntity::class,
        ServiceExpenseEntity::class
    ],
    version = 7,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao  // Aquí está el método que faltaba

    abstract fun clientDao(): ClientDao

    abstract fun saleDao(): SaleDao

    abstract fun purchaseDao(): PurchaseDao

    abstract fun providerDao(): ProviderDao

    abstract fun orderDao(): OrderDao

    abstract fun serviceExpenseDao(): ServiceExpenseDao

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
                    // .addMigrations(MIGRATION_6_7) // Descomenta si tienes migraciones
                    .fallbackToDestructiveMigration() // Puedes añadirlo si quieres eliminar datos en migraciones destructivas
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
