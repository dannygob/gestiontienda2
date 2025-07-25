package com.example.gestiontienda2.presentation.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestiontienda2.domain.models.Order
import com.example.gestiontienda2.domain.usecases.GetOrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderListViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase
) : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _isLoading = MutableStateFlow<List<Order>>(emptyList())
    var isLoading: StateFlow<List<Order>> = _isLoading.asStateFlow()

    private val _errorMessag = MutableStateFlow<List<Order>>(emptyList())
    var errorMessage : StateFlow<List<Order>> = _errorMessag.asStateFlow()



    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        fetchOrders()
    }

    fun fetchOrders() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                getOrdersUseCase.execute().collect { orderList ->
                    _orders.value = orderList
                }
            } catch (e: Exception) {
                _error.value = "Failed to fetch orders: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}