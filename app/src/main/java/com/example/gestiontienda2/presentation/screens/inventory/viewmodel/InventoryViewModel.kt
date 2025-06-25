package com.your_app_name.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.your_app_name.data.local.dao.ProductDao
import com.your_app_name.data.local.entities.ProductEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val productDao: ProductDao
) : ViewModel() {

    private val _products = MutableStateFlow<List<ProductEntity>>(emptyList())
    val products: StateFlow<List<ProductEntity>> = _products.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        getAllProducts()
    }

    fun getAllProducts() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                productDao.getAllProducts().collect { productList ->
                    _products.value = productList
                }
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Error desconocido"
            } finally {
                _loading.value = false
            }
        }
    }

    fun addProduct(product: ProductEntity) {
        viewModelScope.launch {
            try {
                productDao.insertProduct(product)
                getAllProducts()
            } catch (e: Exception) {
                _error.value = "Error al agregar producto: ${e.localizedMessage}"
            }
        }
    }

    fun updateProduct(product: ProductEntity) {
        viewModelScope.launch {
            try {
                productDao.updateProduct(product)
                getAllProducts()
            } catch (e: Exception) {
                _error.value = "Error al actualizar producto: ${e.localizedMessage}"
            }
        }
    }

    fun deleteProduct(product: ProductEntity) {
        viewModelScope.launch {
            try {
                productDao.deleteProduct(product)
                getAllProducts()
            } catch (e: Exception) {
                _error.value = "Error al eliminar producto: ${e.localizedMessage}"
            }
        }
    }

    fun searchProducts(query: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                productDao.searchProducts("%$query%").collect { result ->
                    _products.value = result
                }
            } catch (e: Exception) {
                _error.value = "Error al buscar productos: ${e.localizedMessage}"
            } finally {
                _loading.value = false
            }
        }
    }
}
