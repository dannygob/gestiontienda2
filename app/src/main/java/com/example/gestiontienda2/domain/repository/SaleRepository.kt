package com.gestiontienda2.domain.repository

import com.gestiontienda2.domain.models.Sale
import kotlinx.coroutines.flow.Flow

interface SaleRepository {
    fun getAllSales(): Flow<List<Sale>>
    suspend fun getSaleById(saleId: Int): Sale?
    suspend fun addSale(sale: Sale): Long
    suspend fun updateSale(sale: Sale)
    suspend fun deleteSale(sale: Sale)
    suspend fun getTotalSalesAmount(startDate: Long? = null, endDate: Long? = null): Double
    fun getSalesByDateRange(
        startDate: Long?,
        endDate: Long?
    ): Flow<List<Sale>> // Or Flow<List<SaleWithItems>> if needed
}