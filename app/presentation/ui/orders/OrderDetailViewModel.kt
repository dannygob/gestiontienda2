package app.presentation.ui.orders

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.your_app_name.domain.models.Client
import com.your_app_name.domain.models.Order
import com.your_app_name.domain.models.OrderItem
import com.example.gestiontienda2.domain.models.Product
import com.your_app_name.domain.usecases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getOrderByIdUseCase: GetOrderByIdUseCase,
    private val updateOrderUseCase: UpdateOrderUseCase,
    private val deleteOrderUseCase: DeleteOrderUseCase,
    private val getClientsUseCase: GetClientsUseCase, // Potentially needed for client details display/editing
    private val getProductsUseCase: GetProductsUseCase // Potentially needed for product details display/editing in items
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

    // State for editing
    private val _editingOrder = MutableStateFlow<Order?>(null)
    val editingOrder: StateFlow<Order?> = _editingOrder.asStateFlow()

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
                _editingOrder.value = fetchedOrder // Initialize editing state
            } catch (e: Exception) {
                _error.value = e.message ?: "Error fetching order"
            } finally {
                _loading.value = false
            }
        }
    }

    fun toggleEditMode() {
        _editMode.value = !_editMode.value
        if (_editMode.value) {
            _editingOrder.value = _order.value?.copy() // Create a copy for editing
        } else {
            _editingOrder.value = _order.value // Revert editing state if cancelling edit
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
            // Recalculate total if items change
            updatedOrder.totalAmount = updatedOrder.items.sumOf { it.quantity * it.priceAtOrder }
        }
    }

    // You might need functions to add/remove order items in edit mode
    // fun addOrderItem(...)
    // fun removeOrderItem(itemId: Int)

    fun saveOrder() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            val orderToSave = _editingOrder.value ?: return@launch
            try {
                updateOrderUseCase.execute(orderToSave)
                _order.value = orderToSave // Update the main order state after saving
                _editMode.value = false // Exit edit mode
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
                // Navigate back or show success message after deletion
                // Example: navigateBack() // Assuming navigateBack is passed from the UI
            } catch (e: Exception) {
                _error.value = e.message ?: "Error deleting order"
            } finally {
                _loading.value = false
            }
        }
    }
}
