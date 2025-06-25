package com.yourcompany.app.domain.models

data class SaleDetail(
    val id: Int,
    val saleId: Int,
    val productId: Int,
    val quantity: Int,
    val unitPrice: Double
)