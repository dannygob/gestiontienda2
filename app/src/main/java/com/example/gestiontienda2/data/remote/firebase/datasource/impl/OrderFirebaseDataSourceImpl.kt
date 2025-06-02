package com.your_app_name.data.remote.firebase.datasource.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.your_app_name.data.remote.firebase.models.OrderFirebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderFirebaseDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : com.your_app_name.data.remote.firebase.datasource.OrderFirebaseDataSource {

    private val ordersCollection = firestore.collection("orders")

    override fun getOrders(): Flow<List<OrderFirebase>> = callbackFlow {
        val subscription = ordersCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val orders = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject(OrderFirebase::class.java)?.copy(id = doc.id)
            } ?: emptyList()
            trySend(orders)
        }

        awaitClose { subscription.remove() }
    }

    override suspend fun getOrderById(orderId: String): OrderFirebase? {
        return try {
            ordersCollection.document(orderId).get().await().toObject(OrderFirebase::class.java)
                ?.copy(id = orderId)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun addOrder(order: OrderFirebase) {
        try {
            ordersCollection.add(order).await()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun updateOrder(order: OrderFirebase) {
        try {
            order.id.let { id ->
                ordersCollection.document(id).set(order).await()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun deleteOrder(orderId: String) {
        try {
            ordersCollection.document(orderId).delete().await()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}