package com.your_app_name.domain.usecase

import com.your_app_name.domain.models.Order
import com.your_app_name.domain.repository.OrderRepository
import javax.inject.Inject

class DeleteOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(order: Order) {
        orderRepository.deleteOrder(order)
    }
}