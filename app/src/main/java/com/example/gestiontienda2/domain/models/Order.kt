package com.example.gestiontienda2.domain.models

data class Order(
    val id: Int = 0,
    val clientId: Int,
    val orderDate: Long,
    val status: String,
    var totalAmount: Double,
    val client: Client? = null,
    val items: List<OrderItem> = emptyList(),
)

