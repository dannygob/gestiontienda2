package com.example.gestiontienda2.presentation.ui.products

import androidx.lifecycle.*
import com.example.gestiontienda2.domain.models.Product
import com.example.gestiontienda2.domain.usecases.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                getProductsUseCase().collect { productList ->
                    _products.value = productList.map { productEntity ->
                        // Assuming Product domain model is same as ProductEntity for now
                        // and ProductEntity has stockQuantity and reservedStockQuantity
                        productEntity.copy(
                            availableStock = productEntity.stockQuantity - productEntity.reservedStockQuantity
                        )
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error loading products: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}