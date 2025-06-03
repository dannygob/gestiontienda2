package com.gestiontienda2.data.remote.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

@JvmStatic
interface OpenFoodFactsApiService {

    @GET("/api/v0/product/{barcode}.json")
    suspend fun getProductByBarcode(
        @Path("barcode") barcode: String
    ): Response<OpenFoodFactsProductResponse>

    class OpenFoodFactsProductResponse {

    }
}

// You'll need to define OpenFoodFactsProductResponse data class
// based on the actual API response structure.
// Example placeholder:
/*
data class OpenFoodFactsProductResponse(
    val code: String,
    val product: ProductApi?
)

data class ProductApi(
    val product_name: String?,
    val brands: String?,
    val quantity: String?,
    // Add other relevant fields from the API response
)
*/