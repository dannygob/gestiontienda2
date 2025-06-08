package com.example.gestiontienda2.domain.models

data class ServiceExpense(
    val id: Int,
    val saleId: Int,
    val productId: Int,
    val quantity: Int,
    val unitPrice: Double
)