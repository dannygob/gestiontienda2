package com.example.gestiontienda2.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestiontienda2.domain.models.Client
import com.example.gestiontienda2.domain.models.Product
import com.example.gestiontienda2.domain.models.Sale

import com.example.gestiontienda2.domain.models.SaleItem
import com.example.gestiontienda2.domain.repository.ClientRepository
import com.example.gestiontienda2.domain.repository.ProductRepository
import com.example.gestiontienda2.domain.repository.SaleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Data class to represent the detailed sale state
data class DetailedSaleState(
    val sale: Sale? = null,
    val client: Client? = null,
    val itemsWithProducts: List<SaleItemWithProduct> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val editMode: Boolean = false
)

// Data class to combine SaleItem and Product
data class SaleItemWithProduct(
    val saleItem: SaleItem,
    val product: Product?
)

sealed class SavingState {
    object Idle : SavingState()
    object Saving : SavingState()
    object Success : SavingState()
    data class Error(val message: String) : SavingState()
}

@HiltViewModel
class SaleDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val saleRepository: SaleRepository,
    private val clientRepository: ClientRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val saleId: Int? =
        savedStateHandle["saleId"] // Assuming sale ID is Int and passed as "saleId"

    private val _detailedSaleState = MutableStateFlow(DetailedSaleState(isLoading = true))
    val detailedSaleState: StateFlow<DetailedSaleState> = _detailedSaleState.asStateFlow()

    private val _savingState = MutableStateFlow<SavingState>(SavingState.Idle)
    val savingState: StateFlow<SavingState> = _savingState.asStateFlow()

    init {
        loadSale()
    }

    private fun loadSale() {
        if (saleId == null) {
            _detailedSaleState.value = DetailedSaleState(error = "Sale ID not provided.")
            return
        }

        viewModelScope.launch {
            _detailedSaleState.value = DetailedSaleState(isLoading = true)
            try {
                val sale = saleRepository.getSaleById(saleId)

                if (sale == null) {
                    _detailedSaleState.value = DetailedSaleState(error = "Sale not found.")
                    return@launch
                }

                val client = try {
                    clientRepository.getClientById(sale.clientId)
                } catch (e: Exception) {
                    null // Handle case where client might not exist
                }

                val itemsWithProducts = sale.items.map { item ->
                    val product = try {
                        productRepository.getProductById(item.productId)
                    } catch (e: Exception) {
                        null // Handle case where product might not exist
                    }
                    SaleItemWithProduct(saleItem = item, product = product)
                }

                _detailedSaleState.value = DetailedSaleState(
                    sale = sale,
                    client = client,
                    itemsWithProducts = itemsWithProducts,
                    isLoading = false
                )

            } catch (e: Exception) {
                _detailedSaleState.value = DetailedSaleState(
                    error = e.localizedMessage ?: "Unknown error",
                    isLoading = false
                )
            }
        }
    }

    fun toggleEditMode() {
        _detailedSaleState.value =
            _detailedSaleState.value.copy(editMode = !_detailedSaleState.value.editMode)
    }

    // TODO: Implement updateSaleDetails function
    fun updateSaleDetails(updatedSale: Sale) {
        // Logic to update state and then save
    }

    // TODO: Implement updateSaleItemQuantity function
    fun updateSaleItemQuantity(itemId: Int, newQuantity: Int) {
        // Logic to update quantity in the state
    }

    // TODO: Implement removeSaleItem function
    fun removeSaleItem(itemId: Int) {
        // Logic to remove item from the state
    }

    // TODO: Implement addSaleItem function
    fun addSaleItem(productId: Int, quantity: Int, priceAtSale: Double) {
        // Logic to add a new item to the state
    }


    // TODO: Implement saveSale function
    fun saveSale() {
        viewModelScope.launch {
            _savingState.value = SavingState.Saving
            try {
                val currentDetailedState = _detailedSaleState.value
                val saleToSave =
                    currentDetailedState.sale?.copy(items = currentDetailedState.itemsWithProducts.map { it.saleItem }) // Recreate sale with potentially updated items

                if (saleToSave != null) {
                    // saleRepository.updateSale(saleToSave) // Need updateSale method in repository
                    _savingState.value = SavingState.Success
                } else {
                    _savingState.value = SavingState.Error("Sale data is null.")
                }

            } catch (e: Exception) {
                _savingState.value = SavingState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    // TODO: Implement deleteSale function
    fun deleteSale() {
        viewModelScope.launch {
            _savingState.value = SavingState.Saving // Reuse SavingState for delete
            if (saleId == null) {
                _savingState.value = SavingState.Error("Sale ID not provided for deletion.")
                return@launch
            }
            try {
                saleRepository.deleteSale(saleId) // Need deleteSale method in repository/DAO
                _savingState.value =
                    SavingState.Success // Indicate success, UI should navigate back
            } catch (e: Exception) {
                _savingState.value =
                    SavingState.Error(e.localizedMessage ?: "Failed to delete sale.")
            }
        }
    }
}