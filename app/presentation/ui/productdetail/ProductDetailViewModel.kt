package com.example.app.presentation.ui.productdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.domain.models.Product
import com.example.app.domain.usecases.GetProductByIdUseCase // You'll need to create this
import com.example.app.domain.usecases.UpdateProductUseCase // You'll need to create this
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val updateProductUseCase: UpdateProductUseCase // You'll need to inject this
) : ViewModel() {

    private val productId: Int? = savedStateHandle["productId"] // Assuming product ID is Int and passed as "productId"

 private val _product =
 productRepository.getProductById(productId ?: -1) // Assuming -1 is invalid ID
 .map { it?.copy(availableStock = (it.stockQuantity ?: 0) - (it.reservedStockQuantity ?: 0)) }
 .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    val product: StateFlow<Product?> = _product.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _editMode = MutableStateFlow(false)
    val editMode: StateFlow<Boolean> = _editMode.asStateFlow()

    private val _savingState = MutableStateFlow<SavingState>(SavingState.Idle)
    val savingState: StateFlow<SavingState> = _savingState.asStateFlow()

    init {
        loadProduct()
    }

    private fun loadProduct() {
        // Product is loaded via the stateIn operator in the property initialization
 // We can potentially add a check here or use the product state in the UI to show loading/error
 if (productId == null) {
            _errorMessage.value = "Product ID not provided."
        }
    }

    fun toggleEditMode() {
        _editMode.value = !_editMode.value
    }

    fun saveProduct(updatedProduct: Product) {
        viewModelScope.launch {
            _savingState.value = SavingState.Saving
            try {
                updateProductUseCase(updatedProduct) // Assuming UpdateProductUseCase takes a Product
                _savingState.value = SavingState.Success
            } catch (e: Exception) {
                _errorMessage.value = "Error saving product: ${e.message}" // Consider a separate error state for saving
                _savingState.value = SavingState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class SavingState {
    object Idle : SavingState()
    object Saving : SavingState()
    object Success : SavingState()
    data class Error(val message: String) : SavingState()
}