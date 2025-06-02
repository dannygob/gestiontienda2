package app.domain.usecases

import app.domain.models.Order
import app.domain.repository.OrderRepository
import javax.inject.Inject

class GetOrderByIdUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {

    suspend fun execute(orderId: Int): Order {
        return orderRepository.getOrderById(orderId)
    }
}