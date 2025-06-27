package com.example.gestiontienda2.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestiontienda2.data.local.dao.ProductDao
import com.example.gestiontienda2.data.local.entities.entity.ProductEntity // Corrected path
import com.example.gestiontienda2.domain.models.Product // Domain model
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// Simple mapper for this context. Ideally in a separate mapper file or domain layer.
fun ProductEntity.toDomain(): Product {
    return Product(
        id = this.id,
        name = this.name,
        description = this.description ?: "",
        buyingPrice = this.buyingPrice,
        sellingPrice = this.sellingPrice,
        stockQuantity = this.stockQuantity,
        category = this.category,
        barcode = this.barcode,
        imageUrl = this.imageUrl,
        providerId = this.providerId,
        lowStockThreshold = this.lowStockThreshold,
        reservedStockQuantity = this.reservedStockQuantity // Corrected field access
    )
}


@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val productDao: ProductDao,
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _products = MutableStateFlow<List<Product>>(emptyList()) // Now using domain model
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val filteredProducts: StateFlow<List<Product>> =
        combine(_products, _searchQuery) { products, query ->
            if (query.isBlank()) {
                products
            } else {
                products.filter {
                    it.name.contains(query, ignoreCase = true) ||
                    it.category?.contains(query, ignoreCase = true) == true ||
                    it.barcode?.contains(query, ignoreCase = true) == true
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadProducts()
    }

    fun loadProducts() { // Renamed from getAllProducts and added loading/error handling
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                productDao.getAllProducts().collect { productEntityList ->
                    _products.value = productEntityList.map { it.toDomain() }
                }
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "An unexpected error occurred"
            } finally {
                _loading.value = false
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}