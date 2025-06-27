package com.example.gestiontienda2.domain.repository

import com.example.gestiontienda2.domain.models.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

 fun getAllProducts(): Flow<List<Product>>

    suspend fun getProductById(id: Int): Product?
    suspend fun updateProduct(product: Product) // This line is already present, confirming it remains
    suspend fun deleteProduct(product: Product)

    suspend fun insertProduct(product: Product)
    suspend fun updateProductStockQuantity(productId: Long, newStock: Int)
    suspend fun updateProductReservedStockQuantity(productId: Long, newReservedStock: Int)
}
