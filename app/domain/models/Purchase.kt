package com.yourstoreapp.domain.models

import com.yourstoreapp.domain.models.PurchaseItem

data class Purchase(
    val id: Int = 0,
    val date: Long,
    val providerId: Int, // Corrected field name to match standard
    val totalAmount: Double, // Corrected field name to match standard
    val items: List<PurchaseItem> = emptyList() // Added list of items
)
