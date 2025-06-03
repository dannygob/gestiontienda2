package com.example.gestiontienda2.domain.usecases

import com.example.gestiontienda2.domain.models.Order
import com.example.gestiontienda2.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    fun execute(): Flow<List<Order>> {
        return orderRepository.getOrders()
    }
}