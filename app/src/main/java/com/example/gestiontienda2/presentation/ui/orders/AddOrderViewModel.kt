package com.example.gestiontienda2.presentation.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestiontienda2.domain.models.Client
import com.example.gestiontienda2.domain.models.Order
import com.example.gestiontienda2.domain.models.OrderItem
import com.example.gestiontienda2.domain.models.Product
import com.example.gestiontienda2.domain.usecases.AddOrderUseCase
import com.example.gestiontienda2.domain.usecases.GetClientsUseCase
import com.example.gestiontienda2.domain.usecases.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddOrderViewModel @Inject constructor(
    private val addOrderUseCase: AddOrderUseCase,
    private val getClientsUseCase: GetClientsUseCase,
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val _selectedClient = MutableStateFlow<Client?>(null)
    val selectedClient = _selectedClient.asStateFlow()

    private val _selectedItems = MutableStateFlow<List<OrderItem>>(emptyList())
    val selectedItems = _selectedItems.asStateFlow()

    private val _totalAmount = MutableStateFlow(0.0)
    val totalAmount = _totalAmount.asStateFlow()

    private val _savingState = MutableStateFlow<SavingState>(SavingState.Idle)
    val savingState = _savingState.asStateFlow()

    // State for lists to display in pickers (optional, can be fetched on demand)
    private val _clients = MutableStateFlow<List<Client>>(emptyList())
    val clients = _clients.asStateFlow()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products = _products.asStateFlow()

    private val _orderItems = MutableStateFlow<List<Product>>(emptyList())
    val orderItems = _orderItems.asStateFlow()


    init {
        fetchClients()
        fetchProducts()
    }

    private fun fetchClients() {
        viewModelScope.launch {
            getClientsUseCase.execute().collect {
                _clients.value = it
            }
        }
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            getProductsUseCase.execute().collect {
                _products.value = it
            }
        }
    }


    fun selectClient(client: Client) {
        _selectedClient.value = client
    }

    fun addProductToOrder(product: Product, quantity: Int) {
        val existingItem = _selectedItems.value.find { it.productId == product.id }
        if (existingItem != null) {
            updateItemQuantity(existingItem.product!!, existingItem.quantity + quantity)
        } else {
            _selectedItems.value += OrderItem(
                id = 0, // Will be assigned by Room
                orderId = 0, // Will be assigned when saving the order
                productId = product.id,
                quantity = quantity,
                priceAtOrder = product.price, // Or a negotiated price
                product = product
            )
            calculateTotal()
        }
    }

    private fun updateItemQuantity(product: Product, quantity: Int) {
        _selectedItems.value = _selectedItems.value.map {
            if (it.productId == product.id) {
                it.copy(quantity = quantity)
            } else {
                it
            }
        }
        calculateTotal()
    }

    fun removeItem(item: OrderItem) {
        _selectedItems.value = _selectedItems.value.filter { it.productId != item.productId }
        calculateTotal()
    }

    private fun calculateTotal() {
        _totalAmount.value = _selectedItems.value.sumOf { it.quantity * it.priceAtOrder }
    }

    fun saveOrder(value: Any) {
        viewModelScope.launch {
            _savingState.value = SavingState.Loading
            val client = _selectedClient.value
            val items = _selectedItems.value

            if (client == null || items.isEmpty()) {
                _savingState.value = SavingState.Error("Please select a client and add items.")
                return@launch
            }

            val order = Order(
                id = 0, // Will be assigned by the repository/Room
                clientId = client.id,
                orderDate = System.currentTimeMillis(), // Or use a date picker value
                status = "Pending", // Initial status
                totalAmount = _totalAmount.value,
                items = items
            )

            try {
                addOrderUseCase.execute(order)
                _savingState.value = SavingState.Success
            } catch (e: Exception) {
                _savingState.value = SavingState.Error("Failed to save order: ${e.message}")
            }
        }
    }

    sealed class SavingState {
        object Idle : SavingState()
        object Loading : SavingState()
        object Success : SavingState()
        data class Error(val message: String) : SavingState()
    }
}