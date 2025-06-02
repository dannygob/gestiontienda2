package com.example.gestiontienda2.data.remote.firebase.models

data class PurchaseItemFirebase(
    val productId: String = "",
    val quantity: Int = 0,
    val purchasePrice: Double = 0.0
)