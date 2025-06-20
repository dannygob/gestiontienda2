package com.your_app_name.data.local.room.dao

import androidx.room.*
import com.your_app_name.data.local.room.entities.PurchaseEntity
import com.your_app_name.data.local.room.entities.PurchaseItemEntity
import com.your_app_name.data.local.room.entities.PurchaseWithItems
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchaseDao {

    // Obtener todas las compras con sus ítems
    @Transaction
    @Query("SELECT * FROM purchases")
    fun getAllPurchasesWithItems(): Flow<List<PurchaseWithItems>>

    // Obtener una compra específica con sus ítems
    @Transaction
    @Query("SELECT * FROM purchases WHERE id = :purchaseId")
    suspend fun getPurchaseWithItemsById(purchaseId: Int): PurchaseWithItems?

    // Insertar una compra y devolver su ID generado
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPurchase(purchase: PurchaseEntity): Long

    // Insertar ítems de una compra
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPurchaseItems(items: List<PurchaseItemEntity>)

    // Actualizar una compra
    @Update
    suspend fun updatePurchase(purchase: PurchaseEntity)

    // Eliminar una compra
    @Delete
    suspend fun deletePurchase(purchase: PurchaseEntity)

    // Eliminar ítems asociados a una compra
    @Query("DELETE FROM purchase_items WHERE purchaseId = :purchaseId")
    suspend fun deletePurchaseItemsForPurchase(purchaseId: Int)

    // Obtener el total de compras en un rango de fechas
    @Query("""
        SELECT SUM(totalAmount) FROM purchases
        WHERE (:startDate IS NULL OR purchaseDate >= :startDate)
        AND (:endDate IS NULL OR purchaseDate <= :endDate)
    """)
    suspend fun getTotalPurchaseAmount(startDate: Long?, endDate: Long?): Double

    // Obtener compras con ítems en un rango de fechas
    @Transaction
    @Query("""
        SELECT * FROM purchases
        WHERE (:startDate IS NULL OR purchaseDate >= :startDate)
        AND (:endDate IS NULL OR purchaseDate <= :endDate)
    """)
    fun getPurchasesWithItemsByDateRange(startDate: Long?, endDate: Long?): Flow<List<PurchaseWithItems>>
}
