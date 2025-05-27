package com.example.ministore.data.remote.firebase

import com.example.ministore.data.remote.firebase.PurchaseFirebase

interface PurchaseFirebaseDataSource {
    suspend fun getPurchase(purchaseId: String): PurchaseFirebase?
    suspend fun getAllPurchases(): List<PurchaseFirebase>
    suspend fun addPurchase(purchase: PurchaseFirebase): String?
    suspend fun updatePurchase(purchase: PurchaseFirebase): Boolean
    suspend fun deletePurchase(purchaseId: String): Boolean
}