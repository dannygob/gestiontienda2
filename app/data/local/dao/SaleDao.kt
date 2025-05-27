package com.your_app_name.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.your_app_name.data.local.entities.SaleEntity
import com.your_app_name.data.local.entities.SaleDetailEntity
import com.your_app_name.data.local.entities.SaleWithDetails

@Dao
interface SaleDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSale(sale: SaleEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSaleDetails(saleDetails: List<SaleDetailEntity>)

    @Query("SELECT * FROM sales WHERE id = :saleId")
    suspend fun getSaleById(saleId: Int): SaleEntity?

    @Query("SELECT * FROM sale_details WHERE saleId = :saleId")
    suspend fun getSaleDetailsForSale(saleId: Int): List<SaleDetailEntity>

    @Transaction
    @Query("SELECT * FROM sales")
    suspend fun getAllSalesWithDetails(): List<SaleWithDetails>

    @Update
    suspend fun updateSale(sale: SaleEntity)

    // Add other necessary query methods for sales data

}