package com.example.gestiontienda2.data.remote.firebase

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProductFirebaseDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun getAllProducts(): List<ProductDTO> {
        return try {
            firestore.collection("products")
                .get()
                .await()
                .documents.mapNotNull { document ->
                    document.toObject(ProductDTO::class.java)
                }
        } catch (e: Exception) {
            // Handle error (e.g., network issue, permissions)
            e.printStackTrace()
            emptyList() // Return an empty list in case of error
        }
    }

    suspend fun getProductById(id: Int): ProductDTO? {
        return try {
            firestore.collection("products")
                .document(id.toString())
                .get()
                .await()
                .toObject(ProductDTO::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun addProduct(product: ProductDTO) {
        try {
            firestore.collection("products")
                .document(product.id.toString())
                .set(product)
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun updateProduct(product: ProductDTO) {
        try {
            firestore.collection("products")
                .document(product.id.toString())
                .set(product)
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteProduct(productId: String) {
        try {
            firestore.collection("products")
                .document(productId)
                .delete()
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

// DTO for Firebase Product
data class ProductDTO(
    val id: Int? = null,
    val name: String? = null,
    val description: String? = null,
    val price: Double? = null,
    val stock: Int? = null
)
