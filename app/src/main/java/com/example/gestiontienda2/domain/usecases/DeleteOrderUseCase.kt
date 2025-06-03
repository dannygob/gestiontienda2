package com.example.gestiontienda2.domain.usecases

import com.example.gestiontienda2.domain.models.Order
import com.example.gestiontienda2.domain.repository.OrderRepository
import javax.inject.Inject

class DeleteOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend fun execute(order: Order) {
        orderRepository.deleteOrder(order)
    }
}