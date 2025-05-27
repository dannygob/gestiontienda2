package com.your_app_name.data.remote.firebase.datasource.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.your_app_name.data.remote.firebase.datasource.SaleFirebaseDataSource
import com.your_app_name.data.remote.firebase.models.SaleFirebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaleFirebaseDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : SaleFirebaseDataSource {

    private val salesCollection = firestore.collection("sales")

    override fun getSales(): Flow<List<SaleFirebase>> = callbackFlow {
        val subscription = salesCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val sales = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject(SaleFirebase::class.java)?.copy(id = doc.id)
            } ?: emptyList()
            trySend(sales)
        }

        awaitClose { subscription.remove() }
    }

    override suspend fun getSaleById(saleId: String): SaleFirebase? {
        return try {
            salesCollection.document(saleId).get().await().toObject(SaleFirebase::class.java)?.copy(id = saleId)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun addSale(sale: SaleFirebase) {
        try {
            // Let Firestore generate the ID for new sales
            val newDocRef = salesCollection.document()
            newDocRef.set(sale.copy(id = newDocRef.id)).await()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e // Re-throw the exception to be handled by the calling layer
        }
    }

    override suspend fun updateSale(sale: SaleFirebase) {
        if (sale.id.isBlank()) {
            throw IllegalArgumentException("Sale ID cannot be blank for update")
        }
        try {
            salesCollection.document(sale.id).set(sale).await()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun deleteSale(saleId: String) {
        try {
            salesCollection.document(saleId).delete().await()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}