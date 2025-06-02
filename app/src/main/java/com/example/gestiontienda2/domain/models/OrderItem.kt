package com.your_app_name.domain.models

data class OrderItem(
    val id: Int = 0,
    val orderId: Int,
    val productId: Int,
    val quantity: Int,
    val priceAtOrder: Double,
    val product: Product? = null
)