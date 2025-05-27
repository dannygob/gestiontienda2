package com.example.app.data.remote.firebase

import com.example.app.domain.models.Order
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class OrderFirebaseDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : OrderFirebaseDataSource {

    private val ordersCollection = firestore.collection("orders")

    override suspend fun getOrders(): Flow<List<OrderFirebase>> {
        // Basic implementation: Fetch all documents from the 'orders' collection
        // This will need refinement for real-time updates using snapshots
        return emptyFlow() // Placeholder, implement real fetching logic
    }

    override suspend fun getOrderById(orderId: Int): OrderFirebase? {
        // Basic implementation: Fetch a document by ID
        return try {
            ordersCollection.document(orderId.toString()).get().await().toObject(OrderFirebase::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun addOrder(order: OrderFirebase) {
        // Basic implementation: Add a new document
        ordersCollection.add(order).await()
    }

    override suspend fun updateOrder(order: OrderFirebase) {
        // Basic implementation: Update an existing document by ID
        order.id?.let {
            ordersCollection.document(it.toString()).set(order).await()
        }
    }

    override suspend fun deleteOrder(orderId: Int) {
        // Basic implementation: Delete a document by ID
        ordersCollection.document(orderId.toString()).delete().await()
    }
}