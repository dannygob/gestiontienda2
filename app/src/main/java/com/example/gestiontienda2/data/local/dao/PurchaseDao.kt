package com.example.gestiontienda2.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.gestiontienda2.data.local.entities.entity.PurchaseEntity
import com.example.gestiontienda2.data.local.entities.entity.PurchaseItemEntity
import com.example.gestiontienda2.data.repository.PurchaseWithItems
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchaseDao {

    @Transaction
    @Query("SELECT * FROM purchases")
    fun getAllPurchasesWithItems(): Flow<List<PurchaseWithItems>>

    @Transaction
    @Query("SELECT * FROM purchases WHERE id = :purchaseId")
    suspend fun getPurchaseWithItemsById(purchaseId: Long): PurchaseWithItems?


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPurchase(purchase: PurchaseEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPurchaseItems(items: List<PurchaseItemEntity>)

    @Update
    suspend fun updatePurchase(purchase: PurchaseEntity)

    @Delete
    suspend fun deletePurchase(purchase: PurchaseEntity)

    @Query("DELETE FROM purchase_items WHERE purchaseId = :purchaseId")
    suspend fun deletePurchaseItemsForPurchase(purchaseId: Long)
}
