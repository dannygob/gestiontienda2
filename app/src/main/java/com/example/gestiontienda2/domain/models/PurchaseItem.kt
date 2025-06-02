package com.example.gestiontienda2.domain.models
import android.annotation.SuppressLint
import com.google.android.gms.analytics.ecommerce.Product




data class PurchaseItem @SuppressLint("VisibleForTests") constructor(
    val id: Int = 0,
    val purchaseId: Int,
    val productId: Int,
    val quantity: Int,
    val purchasePrice: Double,
    val product: Product? = null // Optional field to hold associated product details
)