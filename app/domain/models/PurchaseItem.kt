package com.example.gestiontienda2.domain.models

import com.example.gestiontienda2.domain.models.Product

data class PurchaseItem(
    val id: Int = 0,
    val purchaseId: Int,
    val productId: Int,
    val quantity: Int,
    val purchasePrice: Double,
    val product: Product? = null // Optional field to hold associated product details
)
