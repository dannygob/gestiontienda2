package com.example.gestiontienda2.presentation.ui.orders

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestiontienda2.domain.models.Order
import com.example.gestiontienda2.domain.usecases.DeleteOrderUseCase
import com.example.gestiontienda2.domain.usecases.GetClientsUseCase
import com.example.gestiontienda2.domain.usecases.GetOrderByIdUseCase
import com.example.gestiontienda2.domain.usecases.GetProductsUseCase
import com.example.gestiontienda2.domain.usecases.UpdateOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@dagger.assisted.AssistedFactory
interface OrderDetailViewModelFactory {
    fun create(savedStateHandle: SavedStateHandle): OrderDetailViewModel
}

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getOrderByIdUseCase: GetOrderByIdUseCase,
    private val updateOrderUseCase: UpdateOrderUseCase,
    private val deleteOrderUseCase: DeleteOrderUseCase,
    private val getClientsUseCase: GetClientsUseCase,
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val orderId: Int = checkNotNull(savedStateHandle["orderId"])

    private val _order = MutableStateFlow<Order?>(null)
    val order: StateFlow<Order?> = _order.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _editMode = MutableStateFlow(false)
    val editMode: StateFlow<Boolean> = _editMode.asStateFlow()

    private val _editingOrder = MutableStateFlow<Order?>(null)
    val editingOrder: StateFlow<Order?> = _editingOrder.asStateFlow()

    private val _detailedOrder = MutableStateFlow<Order?>(null)
    val detailedOrder: StateFlow<Order?> = _detailedOrder.asStateFlow()

    private val _savingState = MutableStateFlow<Order?>(null)
    val savingState: StateFlow<Order?> = _savingState.asStateFlow()

    init {
        fetchOrder()
    }

    private fun fetchOrder() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val fetchedOrder = getOrderByIdUseCase.execute(orderId)
                _order.value = fetchedOrder
                _editingOrder.value = fetchedOrder
            } catch (e: Exception) {
                _error.value = e.message ?: "Error fetching order"
            } finally {
                _loading.value = false
            }
        }
    }

    fun toggleEditMode() {
        _editMode.value = !_editMode.value
        _editingOrder.value = if (_editMode.value) {
            _order.value?.copy()
        } else {
            _order.value
        }
    }

    fun updateOrderStatus(status: String) {
        _editingOrder.value = _editingOrder.value?.copy(status = status)
    }

    fun updateOrderItemQuantity(itemId: Int, quantity: Int) {
        _editingOrder.value = _editingOrder.value?.copy(
            items = _editingOrder.value?.items?.map { item ->
                if (item.id == itemId) {
                    item.copy(quantity = quantity)
                } else {
                    item
                }
            } ?: emptyList()
        )?.also { updatedOrder ->
            updatedOrder.totalAmount = updatedOrder.items.sumOf { it.quantity * it.priceAtOrder }
        }
    }

    fun saveOrder() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            val orderToSave = _editingOrder.value ?: return@launch
            try {
                updateOrderUseCase.execute(orderToSave)
                _order.value = orderToSave
                _editMode.value = false
            } catch (e: Exception) {
                _error.value = e.message ?: "Error saving order"
            } finally {
                _loading.value = false
            }
        }
    }

    fun deleteOrder() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            val orderToDelete = _order.value ?: return@launch
            try {
                deleteOrderUseCase.execute(orderToDelete)
                // Aquí podrías emitir un evento para navegar hacia atrás
            } catch (e: Exception) {
                _error.value = e.message ?: "Error deleting order"
            } finally {
                _loading.value = false
            }
        }
    }
}
