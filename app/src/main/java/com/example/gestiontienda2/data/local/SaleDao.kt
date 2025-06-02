package com.your_app_name.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.your_app_name.domain.models.Sale
import com.your_app_name.domain.models.SaleItem
import kotlinx.coroutines.flow.Flow

@Dao
interface SaleDao {

    @Insert
    suspend fun insertSale(sale: Sale): Long // Returns the row ID

    @Insert
    suspend fun insertSaleItems(items: List<SaleItem>)

    @Query("SELECT * FROM sales")
    fun getAllSales(): Flow<List<Sale>>

    @Query("SELECT * FROM sales WHERE id = :saleId")
    suspend fun getSaleById(saleId: Int): Sale?

    @Query("SELECT * FROM sale_items WHERE saleId = :saleId")
    suspend fun getSaleItemsBySaleId(saleId: Int): List<SaleItem>

    @Update // Add Update method for Sale
    suspend fun updateSale(sale: Sale)

    @Delete // Add Delete method for Sale
    suspend fun deleteSale(sale: Sale)

    @Query("DELETE FROM sale_items WHERE saleId = :saleId") // Add method to delete items by sale ID
    suspend fun deleteSaleItemsBySaleId(saleId: Int)
}