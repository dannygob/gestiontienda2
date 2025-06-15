package com.example.gestiontienda2.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gestiontienda2.data.local.dao.ClientDao
import com.example.gestiontienda2.data.local.dao.OrderDao
import com.example.gestiontienda2.data.local.dao.ProductDao
import com.example.gestiontienda2.data.local.dao.ProviderDao
import com.example.gestiontienda2.data.local.dao.PurchaseDao
import com.example.gestiontienda2.data.local.dao.SaleDao
import com.example.gestiontienda2.data.local.dao.ServiceExpenseDao
import com.example.gestiontienda2.data.local.room.entities.entity.ClientEntity
import com.example.gestiontienda2.data.local.room.entities.entity.OrderEntity
import com.example.gestiontienda2.data.local.room.entities.entity.OrderItemEntity
import com.example.gestiontienda2.data.local.room.entities.entity.ProductEntity
import com.example.gestiontienda2.data.local.room.entities.entity.ProviderEntity
import com.example.gestiontienda2.data.local.room.entities.entity.PurchaseDetailEntity
import com.example.gestiontienda2.data.local.room.entities.entity.PurchaseEntity
import com.example.gestiontienda2.data.local.room.entities.entity.PurchaseItemEntity
import com.example.gestiontienda2.data.local.room.entities.entity.SaleDetailEntity
import com.example.gestiontienda2.data.local.room.entities.entity.SaleEntity
import com.example.gestiontienda2.data.local.room.entities.entity.SaleItemEntity
import com.example.gestiontienda2.data.local.room.entities.entity.ServiceExpenseEntity
import com.example.gestiontienda2.data.mapper.MyConverters

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
        ServiceExpenseEntity::class,
        SaleItemEntity::class,
        PurchaseItemEntity::class,
        OrderItemEntity::class
    ],
    version = 7,
    exportSchema = false
)
@TypeConverters(MyConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun clientDao(): ClientDao
    abstract fun saleDao(): SaleDao
    abstract fun purchaseDao(): PurchaseDao
    abstract fun providerDao(): ProviderDao
    abstract fun orderDao(): OrderDao
    abstract fun serviceExpenseDao(): ServiceExpenseDao
}
