package com.example.gestiontienda2.presentation.ui.productdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.ministore.domain.usecases.GetProductByIdUseCase
import com.example.gestiontienda2.domain.models.Product
import com.example.gestiontienda2.domain.repository.ProductRepository
import com.example.gestiontienda2.domain.usecases.UpdateProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val updateProductUseCase: UpdateProductUseCase // You'll need to inject this
    private val productRepository: ProductRepository // Inject ProductRepository for stock updates
) : ViewModel() {

    private val productId: Int? =
        savedStateHandle["productId"] // Assuming product ID is Int and passed as "productId"

    private val _stockAdjustmentQuantity = MutableStateFlow("")
    val stockAdjustmentQuantity: StateFlow<String> = _stockAdjustmentQuantity.asStateFlow()

    private val _product =
        getProductByIdUseCase(productId ?: -1) // Assuming -1 is invalid ID
            .map {
                it?.copy(
                    availableStock = (it.stockQuantity ?: 0) - (it.reservedStockQuantity ?: 0)
                )
            }
            // Assuming productRepository is available or injected
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    private val _product = MutableStateFlow<Product?>(null)
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

    fun updateStockAdjustmentQuantity(quantity: String) {
        _stockAdjustmentQuantity.value = quantity
    }

    fun increaseStock() {
        viewModelScope.launch {
            if (_savingState.value is SavingState.Saving || _savingState.value is SavingState.AdjustingStock) {
                return@launch // Prevent multiple attempts
            }

            _savingState.value = SavingState.AdjustingStock
            val quantityToAdjust = _stockAdjustmentQuantity.value.toIntOrNull()

            if (quantityToAdjust == null || quantityToAdjust <= 0) {
                _savingState.value = SavingState.StockAdjustmentError("Invalid quantity.")
                return@launch
            }
            val currentProduct = _product.value
            if (currentProduct == null) {
                _savingState.value = SavingState.StockAdjustmentError("Product not loaded.")
                return@launch
            }

            try {
                // Assuming productRepository has updateStockQuantity method
                productRepository.updateProductStockQuantity(
                    productId ?: -1L,
                    (product.value?.stockQuantity ?: 0) + quantityToAdjust
                ) // Use Long for product ID if that's the type
                _stockAdjustmentState.value = StockAdjustmentState.AdjustedSuccess
                _stockAdjustmentQuantity.value = "" // Clear input on success
            } catch (e: Exception) {
                _stockAdjustmentState.value =
                    StockAdjustmentState.Error("Failed to increase stock: ${e.message}")
                _savingState.value =
                    SavingState.StockAdjustmentError("Failed to increase stock: ${e.message}")
            }
        }
    }

    fun decreaseStock() {
        viewModelScope.launch {
            if (_savingState.value is SavingState.Saving || _savingState.value is SavingState.AdjustingStock) {
                return@launch // Prevent multiple attempts
            }

            _savingState.value = SavingState.AdjustingStock
            val quantityToAdjust = _stockAdjustmentQuantity.value.toIntOrNull()

            if (quantityToAdjust == null || quantityToAdjust <= 0) {
                _savingState.value = SavingState.StockAdjustmentError("Invalid quantity.")
                return@launch
            }
            val currentProduct = _product.value
            if (currentProduct == null) {
                _savingState.value = SavingState.StockAdjustmentError("Product not loaded.")
                return@launch
            }

            try {
                // Add check for sufficient stock before decreasing
                if ((currentProduct.stockQuantity ?: 0) < quantityToAdjust) {
                    _savingState.value = SavingState.StockAdjustmentError("Insufficient stock.")
                    return@launch
                }
                // Assuming productRepository has updateStockQuantity method
                productRepository.updateProductStockQuantity(
                    productId ?: -1L,
                    (product.value?.stockQuantity ?: 0) - quantityToAdjust
                ) // Use Long for product ID if that's the type
                _stockAdjustmentState.value = StockAdjustmentState.AdjustedSuccess
                _savingState.value = SavingState.StockAdjustedSuccess
                _stockAdjustmentQuantity.value = "" // Clear input on success
            } catch (e: Exception) {
                _savingState.value =
                    SavingState.StockAdjustmentError("Failed to decrease stock: ${e.message}")
            }
        }
    }

    fun saveProduct(updatedProduct: Product) {
        viewModelScope.launch {
            _savingState.value = SavingState.Saving
            try {
                updateProductUseCase(updatedProduct) // Assuming UpdateProductUseCase takes a Product
                _savingState.value = SavingState.Success
            } catch (e: Exception) {
                _errorMessage.value =
                    "Error saving product: ${e.message}" // Consider a separate error state for saving
                _savingState.value = SavingState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class SavingState {
    object Idle : SavingState()
    object Saving : SavingState() // For general product saving
    object Success : SavingState() // For general product saving success
    data class Error(val message: String) : SavingState() // For general product saving errors

    object AdjustingStock : SavingState()
    object StockAdjustedSuccess : SavingState()
    data class StockAdjustmentError(val message: String) : SavingState()
}