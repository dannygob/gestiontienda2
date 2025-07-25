package com.example.gestiontienda2.domain.models



data class Product(
    val id: Int,
    val name: String,
    val price: String,
    val barcode: Double,
    val purchasePrice: Double,
    val salePrice: Int,
    val category: String,
    val stock: String,
    val providerId: Int?,
    val stockQuantity: Int,
    val description: Int,
    val buyingPrice: Double,
    val sellingPrice: Double,
    val imageUrl: String? = null,


    ) {
    annotation class SaleItem
}


