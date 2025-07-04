package com.example.gestiontienda2.domain.models

data class PurchaseItem (
    val id: Int = 0,
    val purchaseId: Int,
    val productId: Int,
    val quantity: Int,
    val purchasePrice: Double,
    val product: Product? = null)