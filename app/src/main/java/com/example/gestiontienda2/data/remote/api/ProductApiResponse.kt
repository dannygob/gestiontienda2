package com.example.gestiontienda2.data.remote.api

import com.example.gestiontienda2.domain.models.Product


data class ProductApiResponse(
    val product_name: String?,
    val code: String?,
)

fun ProductApiResponse.toDomainProduct(): Product {
    return Product(
        id = 0,
        name = product_name ?: "Unknown Product",
        barcode = code ?: "",
        purchasePrice = 0.0,
        salePrice = 0.0,
        category = "Unknown",
        stock = 0,
        providerId = 0,
        stockQuantity = 0,
        description = null,
        reservedStockQuantity = 0,
        price = 0.0, // Assuming price is not available in the API response
        availableStock = 0,
        buyingPrice = TODO(),
        sellingPrice = TODO(),
        imageUrl = TODO(), // Assuming available stock is not available in the API response
    )
}
