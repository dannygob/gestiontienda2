package com.your_app_name.domain.repository

import com.your_app_name.domain.models.Order
import kotlinx.coroutines.flow.Flow

interface OrderRepository {

    suspend fun addOrder(order: Order): Long

    fun getAllOrders(): Flow<List<Order>>

    suspend fun getOrderById(orderId: Int): Order?

    suspend fun updateOrder(order: Order)

    suspend fun deleteOrder(order: Order)
}