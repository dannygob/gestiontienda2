package app.domain.usecases

import app.domain.models.Order
import app.domain.repository.OrderRepository
import javax.inject.Inject

class AddOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend fun execute(order: Order): Long {
        return orderRepository.addOrder(order)
    }
}