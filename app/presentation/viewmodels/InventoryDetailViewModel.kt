package com.example.your_app_name.presentation.viewmodels.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.your_app_name.data.repository.ProductRepository
import com.example.your_app_name.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product.asStateFlow()

    fun loadProduct(productId: Int) {
        viewModelScope.launch {
            productRepository.getProductById(productId).collect { product ->
                _product.value = product
            }
        }
    }
}