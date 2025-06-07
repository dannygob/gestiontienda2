package com.example.gestiontienda2.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestiontienda2.domain.models.Order
import com.example.gestiontienda2.domain.models.OrderItem
import com.example.gestiontienda2.domain.models.OrderStatus
import com.example.gestiontienda2.domain.repository.OrderRepository
import com.example.gestiontienda2.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddOrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
) : ViewModel() {

    private val _newOrder = MutableStateFlow(
        Order(
            id = 0,
            clientId = 0,
            orderDate = System.currentTimeMillis(),
            status = OrderStatus.PENDING,
            items = emptyList(),
            totalAmount = 0.0
        )
    )
    val newOrder: StateFlow<Order> = _newOrder.asStateFlow()

    private val _savingState = MutableStateFlow<SavingState>(SavingState.Idle)
    val savingState: StateFlow<SavingState> = _savingState.asStateFlow()

    private val _eventChannel = Channel<UiEvent>()
    val events = _eventChannel.receiveAsFlow()

    fun updateOrderDate(timestamp: Long) {
        _newOrder.value = _newOrder.value.copy(orderDate = timestamp)
    }

    fun updateClient(clientId: Int) {
        _newOrder.value = _newOrder.value.copy(clientId = clientId)
    }

    fun updateStatus(status: OrderStatus) {
        _newOrder.value = _newOrder.value.copy(status = status)
    }

    fun addItem(item: OrderItem) {
        val currentItems = _newOrder.value.items.toMutableList()
        currentItems.add(item)
        _newOrder.value = _newOrder.value.copy(
            items = currentItems,
            totalAmount = calculateTotal(currentItems)
        )
    }

    fun updateItemQuantity(itemId: Int, quantity: Int) {
        val updatedItems = _newOrder.value.items.map { item ->
            if (item.id == itemId) item.copy(quantity = quantity) else item
        }
        _newOrder.value = _newOrder.value.copy(
            items = updatedItems,
            totalAmount = calculateTotal(updatedItems)
        )
    }

    fun removeItem(itemId: Int) {
        val updatedItems = _newOrder.value.items.filterNot { it.id == itemId }
        _newOrder.value = _newOrder.value.copy(
            items = updatedItems,
            totalAmount = calculateTotal(updatedItems)
        )
    }

    private fun calculateTotal(items: List<OrderItem>): Double {
        return items.sumOf { it.quantity * it.priceAtOrder }
    }

    fun saveOrder() {
        viewModelScope.launch {
            _savingState.value = SavingState.Saving

            try {
                val orderToSave = _newOrder.value

                // Validar stock
                for (item in orderToSave.items) {
                    val product = productRepository.getProductById(item.productId)
                    if (product == null || product.stockQuantity - product.reservedStockQuantity < item.quantity) {
                        _eventChannel.send(UiEvent.ShowSnackbar("Stock insuficiente para el producto con ID ${item.productId}"))
                        _savingState.value = SavingState.Idle
                        return@launch
                    }
                }

                // Guardar la orden
                orderRepository.addOrder(orderToSave)
                _savingState.value = SavingState.Success

                // Reservar stock
                for (item in orderToSave.items) {
                    val product = productRepository.getProductById(item.productId)
                    product?.let {
                        productRepository.updateProduct(
                            it.copy(
                                reservedStockQuantity = it.reservedStockQuantity + item.quantity
                            )
                        )
                    }
                }

            } catch (e: Exception) {
                _savingState.value =
                    SavingState.Error("Error al guardar la orden: ${e.localizedMessage}")
            }
        }
    }

    sealed class SavingState {
        object Idle : SavingState()
        object Saving : SavingState()
        object Success : SavingState()
        data class Error(val message: String) : SavingState()
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
    }
}
