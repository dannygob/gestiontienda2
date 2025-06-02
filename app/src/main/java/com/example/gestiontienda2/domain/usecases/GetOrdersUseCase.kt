package app.domain.usecases

import app.domain.models.Order
import app.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    fun execute(): Flow<List<Order>> {
        return orderRepository.getOrders()
    }
}