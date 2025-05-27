package com.your_app_name.data.local.room.dao

import androidx.room.*
import com.your_app_name.data.local.room.entities.SaleEntity
import com.your_app_name.data.local.room.entities.SaleItemEntity
import com.your_app_name.data.local.room.entities.SaleWithItems
import kotlinx.coroutines.flow.Flow

@Dao
interface SaleDao {

    @Transaction
    @Query("SELECT * FROM sales")
    fun getAllSalesWithItems(): Flow<List<SaleWithItems>>

    @Transaction
    @Query("SELECT * FROM sales WHERE id = :saleId")
    suspend fun getSaleWithItemsById(saleId: Int): SaleWithItems?

    @Insert
    suspend fun insertSale(sale: SaleEntity): Long

    @Insert
    suspend fun insertSaleItems(items: List<SaleItemEntity>)

    @Update
    suspend fun updateSale(sale: SaleEntity)

    @Delete
    suspend fun deleteSale(sale: SaleEntity)

    @Query("DELETE FROM saleitems WHERE saleId = :saleId")
    suspend fun deleteSaleItemsForSale(saleId: Int)

    // Optional: Method to update a list of sale items if needed
    @Update
    suspend fun updateSaleItems(items: List<SaleItemEntity>)
}