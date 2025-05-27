package com.your_app_name.domain.repository

import com.your_app_name.domain.models.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

 fun getAllProducts(): Flow<List<Product>>

    suspend fun getProductById(id: Int): Product?
    suspend fun updateProduct(product: Product) // This line is already present, confirming it remains
    suspend fun deleteProduct(product: Product)
}