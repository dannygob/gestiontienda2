package com.example.gestiontienda2.domain.models

import com.example.gestiontienda2.domain.models.Product

data class SaleItem(
    val id: Int = 0,
    val saleId: Int,
    val productId: Int,
    val quantity: Int,
    val priceAtSale: Double,
    val product: Product? = null
)
