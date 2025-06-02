package com.your_app_name.domain.models

data class SaleItem(
    val id: Int = 0,
    val saleId: Int,
    val productId: Int,
    val quantity: Int,
    val priceAtSale: Double,
    val product: Product? = null
)