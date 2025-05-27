package com.your_app_name.data.remote.firebase

interface ProductFirebaseDataSource {

    suspend fun getProduct(productId: String): ProductFirebase?

    suspend fun getAllProducts(): List<ProductFirebase>

    suspend fun addProduct(product: ProductFirebase)

    suspend fun updateProduct(product: ProductFirebase)

    suspend fun deleteProduct(productId: String)
}