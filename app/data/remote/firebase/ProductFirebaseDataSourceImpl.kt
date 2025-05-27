package com.your_app_name.data.remote.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.your_app_name.domain.models.Product
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProductFirebaseDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ProductFirebaseDataSource {

    private val productsCollection = firestore.collection("products")

    override suspend fun getAllProducts(): List<ProductFirebase> {
        return try {
            productsCollection.get().await().documents.mapNotNull { it.toObject<ProductFirebase>() }
        } catch (e: Exception) {
            // Handle exceptions (e.g., network issues, permission denied)
            emptyList()
        }
    }

    override suspend fun getProductById(id: String): ProductFirebase? {
        return try {
            productsCollection.document(id).get().await().toObject<ProductFirebase>()
        } catch (e: Exception) {
            // Handle exceptions
            null
        }
    }

    override suspend fun addProduct(product: ProductFirebase) {
        try {
            productsCollection.document(product.id ?: "").set(product).await()
        } catch (e: Exception) {
            // Handle exceptions
        }
    }

    override suspend fun updateProduct(product: ProductFirebase) {
        try {
            productsCollection.document(product.id ?: "").set(product).await()
        } catch (e: Exception) {
            // Handle exceptions
        }
    }

    override suspend fun deleteProduct(id: String) {
        try {
            productsCollection.document(id).delete().await()
        } catch (e: Exception) {
            // Handle exceptions
        }
    }
}