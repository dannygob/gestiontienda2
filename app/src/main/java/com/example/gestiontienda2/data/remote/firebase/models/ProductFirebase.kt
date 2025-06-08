package com.example.gestiontienda2.data.remote.firebase.models

// Placeholder for Firebase Product model
data class ProductFirebase(
    val id: String = "", // Firestore document ID
    val name: String = "",
    val barcode: String? = null,
    val purchasePrice: Double = 0.0,
    val salePrice: Double = 0.0,
    val category: String? = null,
    val stockQuantity: Int = 0,
    // Note: availableStock and reservedStockQuantity are often calculated,
    // so they might not be directly stored in Firebase unless denormalized.
    // For now, focusing on fields likely to be stored.
    val providerId: String? = null // Assuming providerId is stored as String in Firebase
)
