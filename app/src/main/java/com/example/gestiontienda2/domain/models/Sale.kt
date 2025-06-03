package com.example.gestiontienda2.domain.models


data class Sale(
    val id: Int = 0,
    val saleDate: Long,
    val clientId: Int,
    val totalAmount: Double,
    val items: List<Product.SaleItem>
)