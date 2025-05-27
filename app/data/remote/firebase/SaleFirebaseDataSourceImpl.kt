package com.your_app_name.data.remote.firebase

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class SaleFirebaseDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : SaleFirebaseDataSource {

    private val salesCollection = firestore.collection("sales")

    override suspend fun getSale(saleId: String): SaleFirebase? {
        return try {
            salesCollection.document(saleId).get().await().toObject(SaleFirebase::class.java)
        } catch (e: Exception) {
            // Handle exceptions
            null
        }
    }

    override suspend fun getAllSales(): List<SaleFirebase> {
        return try {
            salesCollection.get().await().toObjects(SaleFirebase::class.java)
        } catch (e: Exception) {
            // Handle exceptions
            emptyList()
        }
    }

    override suspend fun addSale(sale: SaleFirebase): String? {
        return try {
            val documentReference = salesCollection.add(sale).await()
            documentReference.id
        } catch (e: Exception) {
            // Handle exceptions
            null
        }
    }

    override suspend fun updateSale(sale: SaleFirebase) {
        try {
            sale.id?.let { id ->
                salesCollection.document(id).set(sale).await()
            }
        } catch (e: Exception) {
            // Handle exceptions
        }
    }

    override suspend fun deleteSale(saleId: String) {
        try {
            salesCollection.document(saleId).delete().await()
        } catch (e: Exception) {
            // Handle exceptions
        }
    }
}