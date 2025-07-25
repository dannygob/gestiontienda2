package com.example.gestiontienda2.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestiontienda2.domain.models.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryDetailViewModel<ProductRepository> @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    lateinit var error: toString
    private var loading: toBoolean = false
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