package com.gestiontienda2.data.remote.api

import com.gestiontienda2.domain.models.Product

data class ProductApiResponse(
    val product_name: String?,
    val code: String?,
    // Add other relevant fields from the API response here
    // val brands: String?,
    // val quantity: String?,
    // val nutriments: NutrimentsApiResponse?
)

// You might need nested data classes for complex JSON structures, e.g.:
/*
data class NutrimentsApiResponse(
    val energy_kcal_100g: Double?
    // Add other nutriment fields
)
*/

fun ProductApiResponse.toDomainProduct(): Product {
    return Product(
        id = 0, // Placeholder
        name = product_name ?: "Unknown Product",
        barcode = code ?: "",
        purchasePrice = 0.0, // Placeholder
        salePrice = 0.0, // Placeholder
        category = "Unknown", // Placeholder
        stock = 0, // Placeholder
        providerId = 0 // Placeholder
    )
}