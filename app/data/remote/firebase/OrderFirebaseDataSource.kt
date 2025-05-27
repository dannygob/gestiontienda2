package com.your_app_name.data.remote.firebase

import com.your_app_name.data.remote.firebase.OrderFirebase
import com.google.firebase.firestore.FirebaseFirestore

interface OrderFirebaseDataSource {
    suspend fun getOrder(orderId: String): OrderFirebase?
    suspend fun getAllOrders(): List<OrderFirebase>
    suspend fun addOrder(order: OrderFirebase)
    suspend fun updateOrder(order: OrderFirebase)
    suspend fun deleteOrder(orderId: String)
}