package com.your_app_name.domain.repository

import com.your_app_name.domain.models.Purchase
import com.your_app_name.domain.models.PurchaseDetail
import kotlinx.coroutines.flow.Flow

interface PurchaseRepository {
    suspend fun insertPurchaseWithDetails(purchase: Purchase, details: List<PurchaseDetail>)
    fun getAllPurchases(): Flow<List<Purchase>>
    fun getPurchaseById(id: Int): Flow<Purchase?>
    suspend fun getTotalPurchaseAmount(startDate: Long? = null, endDate: Long? = null): Double
    fun getPurchasesByDateRange(startDate: Long? = null, endDate: Long? = null): Flow<List<Purchase>>

    suspend fun updatePurchase(purchase: Purchase)
    suspend fun deletePurchase(purchase: Purchase)
}