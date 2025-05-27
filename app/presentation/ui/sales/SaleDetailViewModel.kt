package com.your_app_name.presentation.ui.sales

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.your_app_name.domain.models.Sale
import com.your_app_name.domain.models.SaleItem
import com.your_app_name.domain.usecases.GetClientsUseCase
import com.your_app_name.domain.usecases.GetProductsUseCase
import com.your_app_name.domain.usecases.GetSaleByIdUseCase
import com.your_app_name.domain.usecases.UpdateSaleUseCase
import com.your_app_name.domain.usecases.DeleteSaleUseCase // Assuming you'll add a delete use case
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SaleDetailState {
    object Loading : SaleDetailState()
    data class Success(val sale: Sale, val editMode: Boolean) : SaleDetailState()
    data class Error(val message: String) : SaleDetailState()
    object Idle : SaleDetailState()
}

sealed class SaleSavingState {
    object Idle : SaleSavingState()
    object Saving : SaleSavingState()
    object Success : SaleSavingState()
    data class Error(val message: String) : SaleSavingState()
}

@HiltViewModel
class SaleDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getSaleByIdUseCase: GetSaleByIdUseCase,
    private val updateSaleUseCase: UpdateSaleUseCase,
    private val deleteSaleUseCase: DeleteSaleUseCase, // Assuming a delete use case
    private val getClientsUseCase: GetClientsUseCase, // Might be needed for client selection in edit
    private val getProductsUseCase: GetProductsUseCase // Might be needed for product selection in edit
) : ViewModel() {

    private val saleId: Int = savedStateHandle["saleId"] ?: -1 // Default or handle error

    private val _saleDetailState = MutableStateFlow<SaleDetailState>(SaleDetailState.Idle)
    val saleDetailState: StateFlow<SaleDetailState> = _saleDetailState.asStateFlow()

    private val _editingSale = MutableStateFlow<Sale?>(null)
    val editingSale: StateFlow<Sale?> = _editingSale.asStateFlow()

    private val _editMode = MutableStateFlow(false)
    val editMode: StateFlow<Boolean> = _editMode.asStateFlow()

    private val _savingState = MutableStateFlow<SaleSavingState>(SaleSavingState.Idle)
    val savingState: StateFlow<SaleSavingState> = _savingState.asStateFlow()

    init {
        if (saleId != -1) {
            fetchSale(saleId)
        } else {
            _saleDetailState.value = SaleDetailState.Error("Invalid Sale ID")
        }
    }

    private fun fetchSale(id: Int) {
        _saleDetailState.value = SaleDetailState.Loading
        viewModelScope.launch {
            try {
                val sale = getSaleByIdUseCase(id)
                if (sale != null) {
                    _saleDetailState.value = SaleDetailState.Success(sale, _editMode.value)
                    _editingSale.value = sale // Initialize editing state
                } else {
                    _saleDetailState.value = SaleDetailState.Error("Sale not found")
                }
            } catch (e: Exception) {
                _saleDetailState.value = SaleDetailState.Error("Error fetching sale: ${e.localizedMessage}")
            }
        }
    }

    fun toggleEditMode() {
        _editMode.value = !_editMode.value
        val currentSale = (_saleDetailState.value as? SaleDetailState.Success)?.sale
        if (currentSale != null) {
            _saleDetailState.value = SaleDetailState.Success(currentSale, _editMode.value)
        }
    }

    fun updateSaleDetails(updatedSale: Sale) {
        _editingSale.value = updatedSale
    }

    fun updateSaleItem(updatedItem: SaleItem) {
        _editingSale.value = _editingSale.value?.copy(
            items = _editingSale.value?.items?.map {
                if (it.id == updatedItem.id) updatedItem else it
            } ?: emptyList()
        )
    }

    fun addSaleItem(newItem: SaleItem) {
        _editingSale.value = _editingSale.value?.copy(
            items = _editingSale.value?.items.orEmpty() + newItem
        )
        // You might need logic to assign a temporary ID or handle new item state
    }

    fun removeSaleItem(itemToRemove: SaleItem) {
        _editingSale.value = _editingSale.value?.copy(
            items = _editingSale.value?.items?.filter { it.id != itemToRemove.id } ?: emptyList()
        )
    }

    fun saveSale() {
        val saleToSave = _editingSale.value ?: return // Cannot save if editingSale is null

        _savingState.value = SaleSavingState.Saving
        viewModelScope.launch {
            try {
                updateSaleUseCase(saleToSave)
                _savingState.value = SaleSavingState.Success
                // Optionally refresh the displayed sale details after saving
                fetchSale(saleId)
            } catch (e: Exception) {
                _savingState.value = SaleSavingState.Error("Error saving sale: ${e.localizedMessage}")
            }
        }
    }

    fun deleteSale() {
        val saleToDelete = (_saleDetailState.value as? SaleDetailState.Success)?.sale ?: return // Cannot delete if sale is not loaded

        viewModelScope.launch {
            try {
                deleteSaleUseCase(saleToDelete)
                // Navigate back or show a success message
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    // Helper function to update total amount based on items
    fun updateSaleTotal() {
        _editingSale.value = _editingSale.value?.copy(
            totalAmount = _editingSale.value?.items?.sumOf { it.quantity * it.priceAtSale } ?: 0.0
        )
    }

    // You might need functions to fetch clients and products for dropdowns/selection in edit mode
}