package com.your_app_name.domain.repository

import com.your_app_name.domain.models.Sale
import com.your_app_name.domain.models.SaleWithDetails
import com.your_app_name.domain.models.SaleItem
import kotlinx.coroutines.flow.Flow

interface SaleRepository {
    fun getAllSales(): Flow<List<Sale>>
    suspend fun getSaleById(saleId: Int): Sale?
    suspend fun addSale(sale: Sale): Long
    suspend fun updateSale(sale: Sale)
    suspend fun deleteSale(sale: Sale)
    suspend fun getTotalSalesAmount(startDate: Long? = null, endDate: Long? = null): Double
    fun getSalesByDateRange(startDate: Long?, endDate: Long?): Flow<List<Sale>> // Or Flow<List<SaleWithItems>> if needed
}