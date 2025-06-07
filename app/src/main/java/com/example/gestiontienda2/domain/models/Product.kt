package com.example.gestiontienda2.domain.models

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val barcode: String,
    val purchasePrice: Double,
    val salePrice: Double,
    val category: String,
    val stock: Int,
    val providerId: Int,
    val stockQuantity: Int,
    val description: String?,
    val reservedStockQuantity: Int,
    val availableStock: Int,

    ) {
    annotation class SaleItem
}


