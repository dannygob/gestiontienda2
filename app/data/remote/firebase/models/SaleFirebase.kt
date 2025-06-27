package com.your_app_name.data.remote.firebase.models

data class SaleFirebase(
    val id: String = "",
    val clientId: String = "",
    val saleDate: Long = 0L,
    val totalAmount: Double = 0.0,
    val saleItems: List<SaleItemFirebase> = emptyList()
)