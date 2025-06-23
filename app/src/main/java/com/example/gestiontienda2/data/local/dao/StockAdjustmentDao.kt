package com.example.gestiontienda2.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gestiontienda2.data.local.entities.entity.StockAdjustmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StockAdjustmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdjustment(adjustment: StockAdjustmentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAdjustments(adjustments: List<StockAdjustmentEntity>)

    @Update
    suspend fun updateAdjustment(adjustment: StockAdjustmentEntity)

    @Delete
    suspend fun deleteAdjustment(adjustment: StockAdjustmentEntity)

    @Query("SELECT * FROM stock_adjustments WHERE id = :adjustmentId")
    suspend fun getAdjustmentById(adjustmentId: Long): StockAdjustmentEntity?

    @Query("SELECT * FROM stock_adjustments")
    fun getAllAdjustments(): Flow<List<StockAdjustmentEntity>>

    @Query("SELECT * FROM stock_adjustments")
    fun getAllAdjustmentsBlocking(): List<StockAdjustmentEntity>
}
