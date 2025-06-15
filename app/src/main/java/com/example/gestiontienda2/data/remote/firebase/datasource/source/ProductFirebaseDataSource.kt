package com.example.gestiontienda2.data.remote.firebase.datasource.source


import com.example.gestiontienda2.domain.models.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductFirebaseDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
) {
    private val productsCollection = firestore.collection("products")

    suspend fun getAllProducts(): List<Product> {
        return try {
            val snapshot = productsCollection.get().await()
            snapshot.documents.mapNotNull { it.toObject(Product::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getProductById(id: String): Product? {
        return try {
            val doc = productsCollection.document(id).get().await()
            doc.toObject(Product::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun addProduct(product: Product) {
        try {
            productsCollection.document(product.id.toString()).set(product).await()
        } catch (_: Exception) {
        }
    }

    suspend fun updateProduct(product: Product) {
        try {
            productsCollection.document(product.id.toString()).set(product).await()
        } catch (_: Exception) {
        }
    }

    suspend fun deleteProduct(id: String) {
        try {
            productsCollection.document(id).delete().await()
        } catch (_: Exception) {
        }
    }
}
