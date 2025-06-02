package com.example.gestiontienda2.data.remote.firebase.datasource

import com.gestiontienda2.data.remote.firebase.models.PurchaseFirebase
import kotlinx.coroutines.flow.Flow

interface PurchaseFirebaseDataSource {

    fun getAllPurchases(): Flow<List<PurchaseFirebase>>

    suspend fun getPurchaseById(purchaseId: String): PurchaseFirebase?

    suspend fun addPurchase(purchase: PurchaseFirebase): String?

    suspend fun updatePurchase(purchase: PurchaseFirebase): Boolean

    suspend fun deletePurchase(purchaseId: String): Boolean
}