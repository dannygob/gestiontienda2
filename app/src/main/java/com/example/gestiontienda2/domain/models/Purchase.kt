package com.example.gestiontienda2.domain.models

data class Purchase(
    val id1: Long,
    val id: Int = 0,
    val date: Long, // Corrected field name to match standard
    val providerId: Int, // Corrected field name to match standard
    val totalAmount: Double,
    val items: List<PurchaseItem> = emptyList(), // Added list of items){}
    val total: Double
)