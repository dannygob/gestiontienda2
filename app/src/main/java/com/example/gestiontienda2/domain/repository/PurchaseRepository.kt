package com.example.gestiontienda2.domain.repository

import com.example.gestiontienda2.domain.models.Purchase
import com.example.gestiontienda2.domain.models.PurchaseDetail
import kotlinx.coroutines.flow.Flow

interface PurchaseRepository {
    suspend fun insertPurchaseWithDetails(purchase: Purchase, details: List<PurchaseDetail>)
    fun getAllPurchases(): Flow<List<Purchase>>
    fun getPurchasesByDateRange(startDate: Long, endDate: Long): Flow<List<Purchase>>
    fun getPurchaseById(id: Int): Flow<Purchase?>
    suspend fun getTotalPurchaseAmount(startDate: Long? = null, endDate: Long? = null): Double
    fun getPurchasesByDateRange(
        startDate: Long? = null,
        endDate: Long? = null
    ): Flow<List<Purchase>>
}