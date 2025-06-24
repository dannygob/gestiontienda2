package com.example.gestiontienda2.data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.gestiontienda2.data.local.room.entities.SaleWithItems
import com.example.gestiontienda2.data.local.room.entities.entity.SaleEntity
import com.example.gestiontienda2.data.local.room.entities.entity.SaleItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SaleDao {

    @Transaction
    @Query("SELECT * FROM sales")
    fun getAllSalesWithItems(): Flow<List<SaleWithItems>>

    @Transaction
    @Query("SELECT * FROM sales WHERE id = :saleId")
    suspend fun getSaleWithItemsById(saleId: Long): SaleWithItems?

    @Insert
    suspend fun insertSale(sale: SaleEntity): Long

    @Insert
    suspend fun insertSaleItems(items: List<SaleItemEntity>)

    @Update
    suspend fun updateSale(sale: SaleEntity)

    @Delete
    suspend fun deleteSale(sale: SaleEntity)

    @Query("DELETE FROM sale_items WHERE saleId = :saleId")
    suspend fun deleteSaleItemsForSale(saleId: Long)

    @Update
    suspend fun updateSaleItems(items: List<SaleItemEntity>)
}
