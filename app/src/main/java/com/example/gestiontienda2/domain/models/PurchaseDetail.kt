package com.example.gestiontienda2.domain.models

data class PurchaseDetail(
    val id: Int = 0,
    val purchaseId: Int,
    val productId: Int,
    val quantity: Int,
    val unitPrice: Double,
    val total: Double
)