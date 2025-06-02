package app.domain.usecases

import app.domain.models.Order
import app.domain.repository.OrderRepository
import javax.inject.Inject

class UpdateOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend fun execute(order: Order) {
        orderRepository.updateOrder(order)
    }
}