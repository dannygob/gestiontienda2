package com.example.gestiontienda2.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestiontienda2.data.local.dao.ProductDao
import com.example.gestiontienda2.data.local.room.entities.entity.ProductEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val productDao: ProductDao,
) : ViewModel() {

    val error: Any = error@ TODO("Handle error state properly")
    private val _products = MutableStateFlow<List<ProductEntity>>(emptyList())
    val products: StateFlow<List<ProductEntity>> = _products.asStateFlow()
    var loading: Any = TODO()

    init {
        getAllProducts()
    }

    private fun getAllProducts() {
        viewModelScope.launch {
            productDao.getAllProducts().collect { productList ->
                _products.value = productList
            }
        }
    }

    // You can add other functions here for inventory management
    // e.g., addProduct, updateProduct, deleteProduct, etc.
}