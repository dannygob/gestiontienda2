package com.gestiontienda2.data.remote.firebase.datasource.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.example.gestiontienda2.data.remote.firebase.datasource.PurchaseFirebaseDataSource
import com.gestiontienda2.data.remote.firebase.models.PurchaseFirebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PurchaseFirebaseDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : PurchaseFirebaseDataSource {

    private val purchasesCollection = firestore.collection("purchases")

    override fun getAllPurchases(): Flow<List<PurchaseFirebase>> = callbackFlow {
        val subscription = purchasesCollection.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                close(exception)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val purchases = snapshot.toObjects(PurchaseFirebase::class.java)
                trySend(purchases).isSuccess
            } else {
                trySend(emptyList()).isSuccess
            }
        }
        awaitClose { subscription.remove() }
    }

    override suspend fun getPurchaseById(purchaseId: String): PurchaseFirebase? {
        return try {
            purchasesCollection.document(purchaseId).get().await()
                .toObject(PurchaseFirebase::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun addPurchase(purchase: PurchaseFirebase): String? {
        return try {
            val documentRef = purchasesCollection.add(purchase).await()
            // Update the document with its own ID if needed (for consistency)
            documentRef.update("id", documentRef.id).await()
            documentRef.id
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun updatePurchase(purchase: PurchaseFirebase): Boolean {
        return try {
            if (purchase.id != null) {
                purchasesCollection.document(purchase.id).set(purchase).await()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun deletePurchase(purchaseId: String): Boolean {
        return try {
            purchasesCollection.document(purchaseId).delete().await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}