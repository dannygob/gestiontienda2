package com.gestiontienda2.domain.repository

import com.gestiontienda2.domain.models.Order
import kotlinx.coroutines.flow.Flow

interface OrderRepository {

    fun getAllOrders(): Flow<List<Order>>

    suspend fun getOrderById(orderId: Int): Order?
    suspend fun addOrder(order: Order): Long

    suspend fun updateOrder(order: Order)

    suspend fun deleteOrder(order: Order)
}