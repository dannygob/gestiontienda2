package com.your_app_name.data.remote.firebase.datasource

import com.your_app_name.data.remote.firebase.models.PurchaseFirebase
import kotlinx.coroutines.flow.Flow

interface PurchaseFirebaseDataSource {

    fun getAllPurchases(): Flow<List<PurchaseFirebase>>

    suspend fun getPurchaseById(purchaseId: String): PurchaseFirebase?

    suspend fun addPurchase(purchase: PurchaseFirebase)

    suspend fun updatePurchase(purchase: PurchaseFirebase)

    suspend fun deletePurchase(purchaseId: String)
}