package com.example.gestiontienda2.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestiontienda2.domain.models.Order
import com.example.gestiontienda2.domain.models.OrderItem
import com.example.gestiontienda2.domain.repository.ClientRepository
import com.example.gestiontienda2.domain.repository.OrderRepository
import com.example.gestiontienda2.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// In OrderDetailViewModel.kt
@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val clientRepository: ClientRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _order = MutableStateFlow<Order?>(null)
    val order: StateFlow<Order?> = _order.asStateFlow()

    data class DetailedOrder(
        val order: Order,
        val clientName: String?,
        val items: List<DetailedOrderItem>
    )

    data class DetailedOrderItem(
        val orderItem: OrderItem,
        val productName: String?
    )

    private val _detailedOrder = MutableStateFlow<DetailedOrder?>(null)
    val detailedOrder: StateFlow<DetailedOrder?> = _detailedOrder.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _editMode = MutableStateFlow(false)
    val editMode: StateFlow<Boolean> = _editMode.asStateFlow()

    private val _savingState = MutableStateFlow<SavingState>(SavingState.Idle)
    val savingState: StateFlow<SavingState> = _savingState.asStateFlow()

    fun loadOrder(orderId: Int) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            orderRepository.getOrderById(orderId).collect { order ->
                _order.value = order
                if (order != null) {
                    val client = clientRepository.getClientById(order.clientId)
                    val detailedItems = order.items.map { item ->
                        val product = productRepository.getProductById(item.productId)
                        DetailedOrderItem(item, product?.name)
                    }
                    _detailedOrder.value = DetailedOrder(order, client?.name, detailedItems)
                    _loading.value = false
                } else {
                    _detailedOrder.value = null
                    _loading.value = false
                    _error.value = "Order not found"
                }
            }
        }
    }

    fun toggleEditMode() {
        _editMode.value = !_editMode.value
    }

    fun updateOrderItemQuantity(itemId: Int, newQuantity: Int) {
        _detailedOrder.value = _detailedOrder.value?.copy(
            items = _detailedOrder.value?.items?.map { detailedItem ->
                if (detailedItem.orderItem.id == itemId) {
                    detailedItem.copy(orderItem = detailedItem.orderItem.copy(quantity = newQuantity))
                } else {
                    detailedItem
                }
            } ?: emptyList()
        )?.also { updatedDetailedOrder ->
            // Recalculate total amount based on updated items
            val newTotal = updatedDetailedOrder.items.sumOf { it.orderItem.quantity * it.orderItem.priceAtOrder }
            _order.value = _order.value?.copy(items = updatedDetailedOrder.items.map { it.orderItem }, totalAmount = newTotal)
        }
    }


    fun removeOrderItem(itemId: Int) {
        _detailedOrder.value = _detailedOrder.value?.copy(
            items = _detailedOrder.value?.items?.filterNot { it.orderItem.id == itemId } ?: emptyList()
        )?.also { updatedDetailedOrder ->
            // Recalculate total amount based on updated items
            val newTotal = updatedDetailedOrder.items.sumOf { it.orderItem.quantity * it.orderItem.priceAtOrder }
            _order.value = _order.value?.copy(items = updatedDetailedOrder.items.map { it.orderItem }, totalAmount = newTotal)
        }
    }


    fun saveOrder() {
        viewModelScope.launch {
            _savingState.value = SavingState.Saving
            try {
                val originalOrder = _order.value // Get the original order
                val updatedOrder = _detailedOrder.value?.order // Get the updated order details

                if (originalOrder != null && updatedOrder != null) {
                    // Calculate quantity changes for each item to adjust reserved stock
                    val reservedStockChanges = mutableMapOf<Int, Int>() // Map of productId to reserved stock change

                    // For items in the updated order, compare with original
                    updatedOrder.items.forEach { updatedItem ->
                        val originalItem = originalOrder.items.find { it.productId == updatedItem.productId }
                        val change = updatedItem.quantity - (originalItem?.quantity ?: 0)
                        reservedStockChanges[updatedItem.productId] = (reservedStockChanges[updatedItem.productId] ?: 0) + change
                    }

                    // For items removed from the original order, decrease their quantity from reserved stock
                    originalOrder.items.forEach { originalItem ->
                        val updatedItem = updatedOrder.items.find { it.productId == originalItem.productId }
                        if (updatedItem == null) {
                             reservedStockChanges[originalItem.productId] = (reservedStockChanges[originalItem.productId] ?: 0) - originalItem.quantity
                        }
                    }

                    // Adjust reserved stock based on quantity changes
                    reservedStockChanges.forEach { (productId, change) ->
                        val product = productRepository.getProductById(productId)
                        product?.let {
                            val updatedReservedStock = it.reservedStockQuantity + change // Adjust reserved stock by the change amount
                            // Ensure reserved stock doesn't go below zero
                             val finalReservedStock = maxOf(0, updatedReservedStock)
                            productRepository.updateProductReservedStockQuantity(it.id, finalReservedStock)
                        }
                    }

                    orderRepository.updateOrder(updatedOrder) // Update the order in the database
                    _savingState.value = SavingState.Success
                    toggleEditMode() // Exit edit mode on success
                } else {
                    _savingState.value = SavingState.Error("Order data is inconsistent for saving")
                }
            } catch (e: Exception) {
                _savingState.value = SavingState.Error(e.localizedMessage ?: "An error occurred")
            }
        }
    }

    fun deleteOrder() {
        viewModelScope.launch {
            _savingState.value = SavingState.Saving
            try {
                val orderToDelete = _order.value
                if (orderToDelete != null) {
                    // Before deleting, release the reserved stock
                    orderToDelete.items.forEach { item ->
                         val product = productRepository.getProductById(item.productId)
                         product?.let {
                              val updatedReservedStock = it.reservedStockQuantity - item.quantity
                               // Ensure reserved stock doesn't go below zero
                              val finalReservedStock = maxOf(0, updatedReservedStock)
                              productRepository.updateProductStockQuantity(it.id, it.stockQuantity + item.quantity) // Assuming return to total stock on delete
                              productRepository.updateProductReservedStockQuantity(it.id, finalReservedStock)
                         }
                    }
                    orderRepository.deleteOrder(orderToDelete)
                    _savingState.value = SavingState.Success // Or a separate state for deletion success
                } else {
                    _savingState.value = SavingState.Error("Order data is inconsistent for deletion")
                }
            } catch (e: Exception) {
                _savingState.value = SavingState.Error(e.localizedMessage ?: "An error occurred")
            }
        }
    }

     fun fulfillOrder() {
        viewModelScope.launch {
            _savingState.value = SavingState.Saving
            try {
                val orderToFulfill = _order.value
                if (orderToFulfill != null && orderToFulfill.status != "FULFILLED") {
                    // Decrease total stock by the reserved quantity and release reserved stock
                    orderToFulfill.items.forEach { item ->
                        val product = productRepository.getProductById(item.productId)
                        product?.let {
                            val updatedTotalStock = it.stockQuantity - item.quantity // Decrease total stock by item quantity
                            val updatedReservedStock = it.reservedStockQuantity - item.quantity // Decrease reserved stock by item quantity
                            // Ensure reserved stock doesn't go below zero
                            val finalReservedStock = maxOf(0, updatedReservedStock)

                            // Use specific repository methods to update stock
                            productRepository.updateProductStockQuantity(it.id, updatedTotalStock)
                            productRepository.updateProductReservedStockQuantity(it.id, finalReservedStock)
                        }
                    }

                    // Update order status to FULFILLED
                    val fulfilledOrder = orderToFulfill.copy(status = "FULFILLED")
                    orderRepository.updateOrder(fulfilledOrder)

                    _savingState.value = SavingState.Success // Or a separate state for fulfillment success
                   // val fulfilledOrder = orderToFulfill.copy(status = "FULFILLED") // duplicate line
                    _order.value = fulfilledOrder // Update local state
                     // Refresh detailed order to show updated status and stock
                     loadOrder(fulfilledOrder.id)

                } else if (orderToFulfill?.status == "FULFILLED") {
                     _savingState.value = SavingState.Error("Order is already fulfilled.")
                } else {
                    _savingState.value = SavingState.Error("Order data is inconsistent for fulfillment")
                }
            } catch (e: Exception) {
                _savingState.value = SavingState.Error(e.localizedMessage ?: "An error occurred")
            }
        }
    }

    fun cancelOrder() {
        viewModelScope.launch {
            _savingState.value = SavingState.Saving
            try {
                val orderToCancel = _order.value
                if (orderToCancel != null && orderToCancel.status != "CANCELLED") {
                    // Return reserved stock to total stock
                    orderToCancel.items.forEach { item ->
                        val product = productRepository.getProductById(item.productId)
                        product?.let {
                            val updatedTotalStock = it.stockQuantity + item.quantity // Return to total stock
                            val updatedReservedStock = it.reservedStockQuantity - item.quantity // Decrease reserved stock
                            // Ensure reserved stock doesn't go below zero
                            val finalReservedStock = maxOf(0, updatedReservedStock)

                            productRepository.updateProductStockQuantity(it.id, updatedTotalStock)
                            productRepository.updateProductReservedStockQuantity(it.id, finalReservedStock)
                        }
                    }

                    orderRepository.updateOrderStatus(orderToCancel.id, "CANCELLED") // Update order status
                    _savingState.value = SavingState.Success // Or a separate state for cancellation success
                    val cancelledOrder = orderToCancel.copy(status = "CANCELLED")
                    _order.value = cancelledOrder // Update local state
                    loadOrder(cancelledOrder.id) // Refresh detailed order
                } else if (orderToCancel?.status == "CANCELLED") {
                    _savingState.value = SavingState.Error("Order is already cancelled.")
                } else {
                    _savingState.value = SavingState.Error("Order data is inconsistent for cancellation")
                }
            } catch (e: Exception) {
                _savingState.value = SavingState.Error(e.localizedMessage ?: "An error occurred")
            }
        }
    }

    enum class SavingState {
        Idle, Saving, Success, Error
    }
}
