package com.example.myapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapp.data.local.entities.StockAdjustmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StockAdjustmentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStockAdjustment(stockAdjustment: StockAdjustmentEntity): Long

    @Query("SELECT * FROM stock_adjustments WHERE productId = :productId ORDER BY timestamp DESC")
    fun getAllStockAdjustmentsForProduct(productId: Int): Flow<List<StockAdjustmentEntity>>

    @Delete
    suspend fun deleteStockAdjustment(stockAdjustment: StockAdjustmentEntity)
}