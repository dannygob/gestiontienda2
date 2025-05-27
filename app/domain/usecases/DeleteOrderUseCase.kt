package app.domain.usecases

import app.domain.models.Order
import app.domain.repository.OrderRepository
import javax.inject.Inject

class DeleteOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend fun execute(order: Order) {
        orderRepository.deleteOrder(order)
    }
}