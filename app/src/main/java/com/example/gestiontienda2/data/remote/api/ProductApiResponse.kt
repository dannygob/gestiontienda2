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
        providerId = 0
    )
}
