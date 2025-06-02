package com.gestiontienda2.presentation.screens.inventory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestiontienda2.data.local.dao.ProductDao
import com.gestiontienda2.data.local.entities.ProductEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val productDao: ProductDao
) : ViewModel() {

    private val _products = MutableStateFlow<List<ProductEntity>>(emptyList())
    val products: StateFlow<List<ProductEntity>> = _products.asStateFlow()

    init {
        getAllProducts()
    }

    fun getAllProducts() {
        viewModelScope.launch {
            productDao.getAllProducts().collect { productList ->
                _products.value = productList
            }
        }
    }

    // You can add other functions here for inventory management
    // e.g., addProduct, updateProduct, deleteProduct, etc.
}