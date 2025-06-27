package com.your_app_name.data.remote.firebase.models

data class PurchaseFirebase(
    val id: String = "",
    val providerId: String = "",
    val purchaseDate: Long = 0L,
    val totalAmount: Double = 0.0,
    val purchaseItems: List<PurchaseItemFirebase> = emptyList()
)