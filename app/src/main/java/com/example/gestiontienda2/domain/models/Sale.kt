package com.example.gestiontienda2.domain.models


data class Sale(
    val id: Int = 0,
    val saleDate: Long,
    val date: String,
    val clientId: Int,
    val totalAmount: Double,
    val paymentMethod: String,
    val total : Double,


    val items: List<Product.SaleItem>
)