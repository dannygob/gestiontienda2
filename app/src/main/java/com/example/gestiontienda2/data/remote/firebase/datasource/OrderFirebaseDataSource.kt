package com.example.gestiontienda2.data.remote.firebase.datasource

import com.example.gestiontienda2.data.remote.firebase.models.OrderFirebase
import kotlinx.coroutines.flow.Flow

interface OrderFirebaseDataSource {
    fun getOrders(): Flow<List<OrderFirebase>>
    suspend fun getOrderById(orderId: String): OrderFirebase?
    suspend fun addOrder(order: OrderFirebase)
    suspend fun updateOrder(order: OrderFirebase)
    suspend fun deleteOrder(orderId: String)
}