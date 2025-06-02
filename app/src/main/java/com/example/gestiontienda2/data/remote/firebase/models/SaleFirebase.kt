package com.gestiontienda2.data.remote.firebase.models

import com.example.gestiontienda2.data.remote.firebase.models.SaleItemFirebase

data class SaleFirebase(
    val id: String = "",
    val clientId: String = "",
    val saleDate: Long = 0L,
    val totalAmount: Double = 0.0,
    val saleItems: List<SaleItemFirebase> = emptyList()
)