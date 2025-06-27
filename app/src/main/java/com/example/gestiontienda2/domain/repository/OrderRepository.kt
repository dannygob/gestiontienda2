package com.example.gestiontienda2.domain.repository

import com.example.gestiontienda2.domain.models.Order
import kotlinx.coroutines.flow.Flow

interface OrderRepository {

    // Devuelve un Flow con lista de órdenes
    fun getOrders(): Flow<List<Order>>

    // Obtiene una orden por id
    suspend fun getOrderById(orderId: Int): Order

    // Agrega una orden, retorna el ID generado (Long)
    suspend fun addOrder(order: Order): Long

    // Actualiza una orden existente
    suspend fun updateOrder(order: Order)

    // Elimina una orden
    suspend fun deleteOrder(order: Order)
}
