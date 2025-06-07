package com.example.gestiontienda2.domain.usecases

import com.your_app_name.domain.models.Order
import com.your_app_name.domain.repository.OrderRepository
import javax.inject.Inject

class AddOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend fun execute(order: Order): Long {
        return orderRepository.addOrder(order)
    }
}
