package com.your_app_name.domain.usecases

import com.your_app_name.domain.models.Order
import com.your_app_name.domain.repository.OrderRepository
import javax.inject.Inject

class GetOrderByIdUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {

    suspend fun execute(orderId: Int): Order? {
        return orderRepository.getOrderById(orderId)
    }
}
