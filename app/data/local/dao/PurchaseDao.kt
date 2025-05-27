package com.your_app_name.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.your_app_name.data.local.entities.PurchaseEntity
import com.your_app_name.data.local.entities.PurchaseDetailEntity

@Dao
interface PurchaseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPurchase(purchase: PurchaseEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPurchaseDetails(details: List<PurchaseDetailEntity>)

    @Query("SELECT * FROM purchases WHERE id = :purchaseId")
    suspend fun getPurchaseById(purchaseId: Int): PurchaseEntity?

    @Query("SELECT * FROM purchase_details WHERE purchaseId = :purchaseId")
    suspend fun getPurchaseDetailsForPurchase(purchaseId: Int): List<PurchaseDetailEntity>

    // You might want to add more queries here, e.g., getting all purchases
    @Query("SELECT * FROM purchases")
    suspend fun getAllPurchases(): List<PurchaseEntity>
}