package com.your_app_name.presentation.ui.productdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestiontienda2.domain.models.Product
import com.your_app_name.domain.repository.ProductRepository // Import ProductRepository
import com.your_app_name.domain.usecases.GetProductByIdUseCase // You'll need to create this
import com.your_app_name.domain.usecases.UpdateProductUseCase // You'll need to create this
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
// import kotlinx.coroutines.flow.map // Not strictly needed with new loadProduct
// import kotlinx.coroutines.flow.stateIn // Not strictly needed with new loadProduct
// import kotlinx.coroutines.flow.SharingStarted // Not strictly needed with new loadProduct
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val updateProductUseCase: UpdateProductUseCase, // You'll need to inject this
    private val productRepository: ProductRepository // Inject ProductRepository for stock updates
) : ViewModel() {

    private val productId: Int? = savedStateHandle["productId"] // Assuming product ID is Int and passed as "productId"

    private val _stockAdjustmentQuantity = MutableStateFlow("")
    val stockAdjustmentQuantity: StateFlow<String> = _stockAdjustmentQuantity.asStateFlow()

    private val _product = MutableStateFlow<Product?>(null) // Initialize as MutableStateFlow
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
        if (productId == null) {
            _errorMessage.value = "Product ID not provided."
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Assuming getProductByIdUseCase returns Flow<Product?>
                getProductByIdUseCase(productId).collect { productFromUseCase ->
                    _product.value = productFromUseCase?.copy(
                        availableStock = (productFromUseCase.stockQuantity ?: 0) - (productFromUseCase.reservedStockQuantity ?: 0)
                    ) // Assuming Product has stockQuantity, reservedStockQuantity, availableStock
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error loading product: ${e.message}"
            } finally {
                _isLoading.value = false
            }
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
                // Assuming productRepository has updateProductStockQuantity method
                productRepository.updateProductStockQuantity(productId?.toLong() ?: -1L, (_product.value?.stockQuantity ?: 0) + quantityToAdjust)
                _savingState.value = SavingState.StockAdjustedSuccess // Updated state
                _stockAdjustmentQuantity.value = "" // Clear input on success
            } catch (e: Exception) {
                _savingState.value = SavingState.StockAdjustmentError("Failed to increase stock: ${e.message}")
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
                productRepository.updateProductStockQuantity(productId?.toLong() ?: -1L, (_product.value?.stockQuantity ?: 0) - quantityToAdjust)
                _savingState.value = SavingState.StockAdjustedSuccess
                _stockAdjustmentQuantity.value = "" // Clear input on success
            } catch (e: Exception) {
                _savingState.value = SavingState.StockAdjustmentError("Failed to decrease stock: ${e.message}")
            }
        }
    }

    fun saveProduct() {
        val productToSave = _product.value ?: return
        viewModelScope.launch {
            _savingState.value = SavingState.Saving
            try {
                updateProductUseCase(productToSave) // Assuming UpdateProductUseCase takes a Product
                _savingState.value = SavingState.Success
                _editMode.value = false // Exit edit mode on successful save
            } catch (e: Exception) {
                _errorMessage.value = "Error saving product: ${e.message}" // Consider a separate error state for saving
                _savingState.value = SavingState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun updateProductName(name: String) { _product.value = _product.value?.copy(name = name) }
    fun updateProductBarcode(barcode: String) { _product.value = _product.value?.copy(barcode = barcode) }
    fun updateProductPurchasePrice(price: Double) { _product.value = _product.value?.copy(purchasePrice = price) }
    fun updateProductSalePrice(price: Double) { _product.value = _product.value?.copy(salePrice = price) }
    fun updateProductCategory(category: String) { _product.value = _product.value?.copy(category = category) }
    fun updateProductStock(stock: Int) { _product.value = _product.value?.copy(stockQuantity = stock) } // Assuming 'stock' maps to 'stockQuantity'
    fun updateProductProviderId(providerId: String) { _product.value = _product.value?.copy(providerId = providerId) }
}

sealed class SavingState {
    object Idle : SavingState()
    object Saving : SavingState() // For general product saving
    object Success : SavingState() // For general product saving success
    data class Error(val message: String) : SavingState() // For general product saving errors

    object AdjustingStock : SavingState() // Indicates stock adjustment is in progress
    object StockAdjustedSuccess : SavingState() // Indicates stock adjustment was successful
    data class StockAdjustmentError(val message: String) : SavingState() // For stock adjustment errors
}
