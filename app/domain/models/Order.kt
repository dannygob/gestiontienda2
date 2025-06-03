package com.yourcompany.yourappname.domain.models

import com.yourcompany.yourappname.domain.models.OrderItem

data class Order(
    val id: Int = 0,
    val clientId: Int,
    val orderDate: Long,
    val status: String,
    val totalAmount: Double,
    val items: List<OrderItem> = emptyList()
)
