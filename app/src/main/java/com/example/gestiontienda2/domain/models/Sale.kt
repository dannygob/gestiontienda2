package com.yourcompany.app.domain.models

import com.yourcompany.app.domain.models.SaleItem

data class Sale(
    val id: Int = 0,
    val saleDate: Long,
    val clientId: Int,
    val totalAmount: Double,
    val items: List<SaleItem>
)