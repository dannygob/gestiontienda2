package com.your_app_name.domain.repository

import com.your_app_name.domain.models.Purchase
import com.your_app_name.domain.models.PurchaseWithDetails // Assuming you'll have a model for this
import kotlinx.coroutines.flow.Flow

interface PurchaseRepository {
    fun getAllPurchases(): Flow<List<Purchase>>
    suspend fun getPurchaseById(purchaseId: Int): Purchase?
    suspend fun addPurchase(purchase: Purchase): Long
    suspend fun updatePurchase(purchase: Purchase)
    suspend fun deletePurchase(purchase: Purchase)
}