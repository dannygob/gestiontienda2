package com.example.gestiontienda2.data.remote.firebase.datasource.source

import com.example.gestiontienda2.data.remote.firebase.models.OrderFirebase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderFirebaseDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : OrderFirebaseDataSource {

    private val ordersCollection = firestore.collection("orders")

    override fun getOrders(): Flow<List<OrderFirebase>> = flow {
        try {
            val snapshot = ordersCollection.get().await()
            val orders = snapshot.documents.mapNotNull { it.toObject(OrderFirebase::class.java) }
            emit(orders)
        } catch (e: Exception) {
            // Log error or emit empty list
            emit(emptyList())
        }
    }

    override suspend fun getOrderById(orderId: String): OrderFirebase? {
        return try {
            val document = ordersCollection.document(orderId).get().await()
            document.toObject(OrderFirebase::class.java)
        } catch (e: Exception) {
            // Log error
            null
        }
    }

    override suspend fun addOrder(order: OrderFirebase) {
        try {
            // If order.id is blank, Firestore generates one.
            // If you want to enforce client-generated IDs or map to a specific field, adjust here.
            ordersCollection.document(order.id.ifBlank { ordersCollection.document().id }).set(order).await()
        } catch (e: Exception) {
            // Log error
            throw e // Or handle more gracefully
        }
    }

    override suspend fun updateOrder(order: OrderFirebase) {
        try {
            ordersCollection.document(order.id).set(order).await()
        } catch (e: Exception) {
            // Log error
            throw e // Or handle more gracefully
        }
    }

    override suspend fun deleteOrder(orderId: String) {
        try {
            ordersCollection.document(orderId).delete().await()
        } catch (e: Exception) {
            // Log error
            throw e // Or handle more gracefully
        }
    }
}
