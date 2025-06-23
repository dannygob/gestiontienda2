package com.your_app_name.presentation.screens.inventory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.your_app_name.data.local.dao.ProductDao
import com.your_app_name.data.local.entities.ProductEntity
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

    fun addProduct(product: ProductEntity) {
        viewModelScope.launch {
            productDao.insertProduct(product)
        }
    }

    fun updateProduct(product: ProductEntity) {
        viewModelScope.launch {
            productDao.updateProduct(product)
        }
    }

    fun deleteProduct(product: ProductEntity) {
        viewModelScope.launch {
            productDao.deleteProduct(product)
        }
    }

    fun searchProducts(query: String) {
        viewModelScope.launch {
            productDao.searchProducts("%$query%").collect { result ->
                _products.value = result
            }
        }
    }
}
