package com.example.gestiontienda2.data.remote.api

import com.example.gestiontienda2.domain.models.Product
import kotlin.Int


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
        imageUrl = null, // Assuming imageUrl is not available in the API response
        location = "Unknown Location", // Assuming location is not available in the API response
        isActive = true, // Assuming the product is active by default
        isFavorite = false, // Assuming the product is not a favorite by default
        isInCart = false, // Assuming the product is not in the cart by default
        price = 0.0, // Assuming price is not available in the API response
        stock = 0, // Assuming stock is not available in the API response
        availableStock = 0, // Assuming available stock is not available in the API response
        )
}
