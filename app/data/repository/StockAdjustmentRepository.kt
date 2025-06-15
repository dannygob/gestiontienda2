package com.example.myapp.data.local.repository

import com.example.myapp.data.local.dao.StockAdjustmentDao
import com.example.myapp.data.local.entities.StockAdjustmentEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockAdjustmentRepository @Inject constructor(
    private val stockAdjustmentDao: StockAdjustmentDao
) {

    suspend fun insertStockAdjustment(stockAdjustment: StockAdjustmentEntity): Long {
        return stockAdjustmentDao.insertStockAdjustment(stockAdjustment)
    }

    fun getAllStockAdjustmentsForProduct(productId: Int): Flow<List<StockAdjustmentEntity>> {
        return stockAdjustmentDao.getAllStockAdjustmentsForProduct(productId)
    }

    suspend fun deleteStockAdjustment(stockAdjustment: StockAdjustmentEntity) {
        stockAdjustmentDao.deleteStockAdjustment(stockAdjustment)
    }
}
