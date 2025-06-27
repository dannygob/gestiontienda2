package com.example.gestiontienda2.data.remote.api

import com.example.gestiontienda2.domain.models.Product


data class ProductApiResponse(
    val product_name: String?,
    val code: String?,
)

fun ProductApiResponse.toDomain(): Product {
    return Product(
        id = 0, // Assuming ID is not provided in the API response
        name = product_name ?: "",
        price = 0.0, // Default value, adjust as necessary
        stock = 0, // Default value, adjust as necessary
        barcode = code ?: "",
        purchasePrice = 0.0, // Default value, adjust as necessary
        salePrice = 0.0, // Default value, adjust as necessary
        category = "", // Default value, adjust as necessary
        providerId = 0,
        stockQuantity = TODO(),
        description = TODO(),
        buyingPrice = TODO(),
        sellingPrice = TODO(),
        imageUrl = TODO() // Default value, adjust as necessary
    )
}