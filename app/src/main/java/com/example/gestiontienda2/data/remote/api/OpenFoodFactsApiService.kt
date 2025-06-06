package com.example.gestiontienda2.data.remote.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface OpenFoodFactsApiService {

    @GET("api/v0/product/{barcode}.json")
    suspend fun getProductByBarcode(
        @Path("barcode") barcode: String
    ): Response<OpenFoodFactsProductResponse>
}

// Modelos de datos basados en la estructura JSON de la API
data class OpenFoodFactsProductResponse(
    val code: String?,
    val status: Int?,
    val product: ProductApi?,
)

data class ProductApi(
    val product_name: String?,
    val brands: String?,
    val quantity: String?,
    val image_url: String?,
    val categories: String?,
    // Puedes agregar más campos que te interesen del JSON
)
