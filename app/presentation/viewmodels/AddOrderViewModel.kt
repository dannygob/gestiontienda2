package com.your_app_name.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.your_app_name.domain.models.Order
import com.your_app_name.domain.models.OrderItem
import com.your_app_name.domain.models.OrderStatus
import com.your_app_name.domain.repository.OrderRepository
import com.your_app_name.domain.repository.ProductRepository
import com.your_app_name.presentation.ui.UiEvent
import com.your_app_name.util.SavingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddOrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _newOrder = MutableStateFlow(
        Order(
            id = 0,
            clientId = -1, // Usar -1 para indicar cliente no seleccionado
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
    val events: Flow<UiEvent> = _eventChannel.receiveAsFlow()

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
        val updatedItems = _newOrder.value.items + item
        _newOrder.value = _newOrder.value.copy(
            items = updatedItems,
            totalAmount = calculateTotal(updatedItems)
        )
    }

    fun updateItemQuantity(itemId: Int, quantity: Int) {
        val updatedItems = _newOrder.value.items.map {
            if (it.id == itemId) it.copy(quantity = quantity) else it
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

    private suspend fun validateOrder(): Boolean {
        val order = _newOrder.value

        if (order.clientId < 0) {
            _eventChannel.send(UiEvent.ShowSnackbar("Selecciona un cliente."))
            return false
        }

        if (order.items.isEmpty()) {
            _eventChannel.send(UiEvent.ShowSnackbar("Agrega al menos un producto."))
            return false
        }

        return withContext(Dispatchers.IO) {
            for (item in order.items) {
                val product = productRepository.getProductById(item.productId)
                if (product == null || product.stockQuantity - product.reservedStockQuantity < item.quantity) {
                    _eventChannel.send(UiEvent.ShowSnackbar("Stock insuficiente para el producto con ID ${item.productId}."))
                    return@withContext false
                }
            }
            true
        }
    }

    fun saveOrder() {
        viewModelScope.launch {
            _savingState.value = SavingState.Saving

            try {
                if (!validateOrder()) {
                    _savingState.value = SavingState.Idle
                    return@launch
                }

                val orderId = withContext(Dispatchers.IO) {
                    orderRepository.insertOrder(_newOrder.value)
                }

                _savingState.value = SavingState.Success

                // Reservar stock en bloque
                coroutineScope {
                    _newOrder.value.items.forEach { item ->
                        launch(Dispatchers.IO) {
                            val product = productRepository.getProductById(item.productId)
                            product?.let {
                                val updatedProduct = it.copy(
                                    reservedStockQuantity = it.reservedStockQuantity + item.quantity
                                )
                                productRepository.updateProduct(updatedProduct)
                            }
                        }
                    }
                }

                _eventChannel.send(UiEvent.ShowSnackbar("Pedido guardado con éxito."))

            } catch (e: Exception) {
                _savingState.value = SavingState.Error("Error al guardar el pedido: ${e.localizedMessage}")
                _eventChannel.send(UiEvent.ShowSnackbar("Error al guardar el pedido."))
            }
        }
    }
}
