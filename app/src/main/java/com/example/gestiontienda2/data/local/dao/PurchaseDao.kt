package com.gestiontienda2.data.local.dao

import androidx.room.*
import com.gestiontienda2.data.local.room.entities.PurchaseEntity
import com.gestiontienda2.data.local.room.entities.PurchaseItemEntity
import com.gestiontienda2.data.local.room.entities.PurchaseWithItems
import kotlinx.coroutines.flow.Flow

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