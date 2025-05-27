package com.your_app_name.domain.usecase

import com.your_app_name.domain.models.Order
import com.your_app_name.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllOrdersUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    operator fun invoke(): Flow<List<Order>> {
        return orderRepository.getOrders()
    }
}