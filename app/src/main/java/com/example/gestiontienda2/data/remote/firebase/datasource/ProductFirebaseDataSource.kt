package com.example.gestiontienda2.data.remote.firebase.datasource

import com.example.gestiontienda2.data.remote.firebase.models.ProductFirebase
import kotlinx.coroutines.flow.Flow

interface ProductFirebaseDataSource {
    fun getAllProducts(): Flow<List<ProductFirebase>>
    suspend fun getProductById(productId: String): ProductFirebase?
    suspend fun addProduct(product: ProductFirebase)
    suspend fun updateProduct(product: ProductFirebase)
    suspend fun deleteProduct(productId: String) // Often by ID
    // May need methods for stock updates if not handled via general updateProduct
    // suspend fun updateStock(productId: String, newStock: Int)
}
