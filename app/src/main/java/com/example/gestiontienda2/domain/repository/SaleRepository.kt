package com.example.gestiontienda2.domain.repository

import com.example.gestiontienda2.domain.models.Sale
import com.example.gestiontienda2.domain.models.SaleDetail
import kotlinx.coroutines.flow.Flow

interface SaleRepository {
    fun getAllSales(): Flow<List<Sale>>
    suspend fun getSaleById(saleId: Int): Sale?
    suspend fun addSale(sale: Sale): Long
    suspend fun updateSale(sale: Sale)
    suspend fun deleteSale(sale: Int)
    suspend fun getTotalSalesAmount(startDate: Long? = null, endDate: Long? = null): Double
    fun getSalesByDateRange(
        startDate: Long?,
        endDate: Long?
    ): Flow<List<Sale>> // Or Flow<List<SaleWithItems>> if needed

    fun insertSaleWithDetails   (sale: Sale, saleDetails: List<SaleDetail>)
    suspend fun getSales(): Flow<List<Sale>>
}