package com.gestiontienda2.data.remote.firebase.models

data class SaleItemFirebase(
    val productId: String = "",
    val quantity: Int = 0,
    val priceAtSale: Double = 0.0
)