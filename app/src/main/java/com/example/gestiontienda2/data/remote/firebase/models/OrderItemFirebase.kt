package com.example.gestiontienda2.data.remote.firebase.models

data class OrderItemFirebase(
    val productId: String = "",
    val quantity: Int = 0,
    val priceAtOrder: Double = 0.0
)