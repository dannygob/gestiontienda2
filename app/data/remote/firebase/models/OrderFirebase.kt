package com.your_app_name.data.remote.firebase.models

import com.your_app_name.data.remote.firebase.models.OrderItemFirebase // Assuming OrderItemFirebase is in the same package

data class OrderFirebase(
    val id: String = "",
    val clientId: String = "",
    val orderDate: Long = 0L,
    val status: String = "", // e.g., "pending", "processing", "completed", "cancelled"
    val totalAmount: Double = 0.0,
    val orderItems: List<OrderItemFirebase> = emptyList()
)