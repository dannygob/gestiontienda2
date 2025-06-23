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
import com.example.gestiontienda2.data.local.dao.StockAdjustmentDao
import com.example.gestiontienda2.data.local.entities.entity.ClientEntity
import com.example.gestiontienda2.data.local.entities.entity.OrderEntity
import com.example.gestiontienda2.data.local.entities.entity.OrderItemEntity
import com.example.gestiontienda2.data.local.entities.entity.ProductEntity
import com.example.gestiontienda2.data.local.entities.entity.ProviderEntity
import com.example.gestiontienda2.data.local.entities.entity.PurchaseDetailEntity
import com.example.gestiontienda2.data.local.entities.entity.PurchaseEntity
import com.example.gestiontienda2.data.local.entities.entity.PurchaseItemEntity
import com.example.gestiontienda2.data.local.entities.entity.SaleDetailEntity
import com.example.gestiontienda2.data.local.entities.entity.SaleEntity
import com.example.gestiontienda2.data.local.entities.entity.SaleItemEntity
import com.example.gestiontienda2.data.local.entities.entity.ServiceExpenseEntity
import com.example.gestiontienda2.data.local.entities.entity.StockAdjustmentEntity
import com.example.gestiontienda2.data.mapper.MyConverters

@Database(
    entities = [
        ClientEntity::class,
        ProductEntity::class,
        SaleEntity::class,
        SaleDetailEntity::class,
        PurchaseEntity::class,
        PurchaseDetailEntity::class,
        ProviderEntity::class,
        OrderEntity::class,
        OrderItemEntity::class,
        ServiceExpenseEntity::class,
        SaleItemEntity::class,
        PurchaseItemEntity::class,
        StockAdjustmentEntity::class
    ],
    version = 8,
    exportSchema = false
)
@TypeConverters(MyConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clientDao(): ClientDao
    abstract fun productDao(): ProductDao
    abstract fun saleDao(): SaleDao
    abstract fun purchaseDao(): PurchaseDao
    abstract fun providerDao(): ProviderDao
    abstract fun orderDao(): OrderDao
    abstract fun serviceExpenseDao(): ServiceExpenseDao
    abstract fun stockAdjustmentDao(): StockAdjustmentDao
}
