package com.example.gestiontienda2.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.gestiontienda2.data.local.room.entities.SaleEntity
import com.example.gestiontienda2.data.local.room.entities.SaleItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SaleDao {

    @Insert
    suspend fun insertSale(sale: SaleEntity): Long

    @Insert
    suspend fun insertSaleItems(items: List<SaleItemEntity>)

    @Query("SELECT * FROM sales")
    fun getAllSales(): Flow<List<SaleEntity>>

    @Query("SELECT * FROM sales WHERE id = :saleId")
    suspend fun getSaleById(saleId: Long): SaleEntity?

    @Query("SELECT * FROM sale_items WHERE saleId = :saleId")
    suspend fun getSaleItemsBySaleId(saleId: Long): List<SaleItemEntity>

    @Update
    suspend fun updateSale(sale: SaleEntity)

    @Delete
    suspend fun deleteSale(sale: SaleEntity)

    @Query("DELETE FROM sale_items WHERE saleId = :saleId")
    suspend fun deleteSaleItemsBySaleId(saleId: Long)
}
