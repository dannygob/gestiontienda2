package com.example.gestiontienda2.domain.repository

import com.your_app_name.domain.models.Order
import kotlinx.coroutines.flow.Flow

interface OrderRepository {

    fun getAllOrders(): Flow<List<Order>>

    suspend fun getOrderById(orderId: Int): Order?
    suspend fun addOrder(order: Order): Long

    suspend fun updateOrder(order: Order)

    suspend fun deleteOrder(order: Order)

    suspend fun updateOrderStatus(orderId: Long, newStatus: String)
}
