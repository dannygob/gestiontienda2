package com.example.gestiontienda2.data.repository


import com.example.gestiontienda2.data.local.dao.StockAdjustmentDao
import com.example.gestiontienda2.data.local.entities.entity.StockAdjustmentEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockAdjustmentRepository @Inject constructor(
    private val stockAdjustmentDao: StockAdjustmentDao
) {

    suspend fun insertStockAdjustment(stockAdjustment: StockAdjustmentEntity): Long {
        return withContext(Dispatchers.IO) {
            try {
                stockAdjustmentDao.insertStockAdjustment(stockAdjustment)
            } catch (e: Exception) {
                e.printStackTrace()
                -1L // Retorna un valor indicativo de error
            }
        }
    }

    fun getAllStockAdjustmentsForProduct(productId: Int): Flow<List<StockAdjustmentEntity>> {
        return stockAdjustmentDao.getAllStockAdjustmentsForProduct(productId)
    }

    suspend fun deleteStockAdjustment(stockAdjustment: StockAdjustmentEntity) {
        withContext(Dispatchers.IO) {
            try {
                stockAdjustmentDao.deleteStockAdjustment(stockAdjustment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
