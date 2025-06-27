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

data class DetailedSaleState(
    val sale: Sale? = null,
    val client: Client? = null,
    val itemsWithProducts: List<SaleItemWithProduct> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val editMode: Boolean = false
)

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

    private val saleId: Int? = savedStateHandle["saleId"]

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
                    ?: throw Exception("Sale not found")

                val client = try {
                    clientRepository.getClientById(sale.clientId)
                } catch (e: Exception) {
                    null
                }

                val itemsWithProducts = sale.items.map { item ->
                    val product = try {
                        productRepository.getProductById(item.productId)
                    } catch (e: Exception) {
                        null
                    }
                    SaleItemWithProduct(item, product)
                }

                _detailedSaleState.value = DetailedSaleState(
                    sale = sale,
                    client = client,
                    itemsWithProducts = itemsWithProducts,
                    isLoading = false
                )

            } catch (e: Exception) {
                _detailedSaleState.value = DetailedSaleState(
                    error = e.localizedMessage ?: "Error loading sale",
                    isLoading = false
                )
            }
        }
    }

    fun toggleEditMode() {
        _detailedSaleState.value =
            _detailedSaleState.value.copy(editMode = !_detailedSaleState.value.editMode)
    }

    fun updateSaleDetails(updatedSale: Sale) {
        _detailedSaleState.value = _detailedSaleState.value.copy(sale = updatedSale)
    }

    fun updateSaleItemQuantity(itemId: Int, newQuantity: Int) {
        val updatedItems = _detailedSaleState.value.itemsWithProducts.map {
            if (it.saleItem.id == itemId) {
                it.copy(saleItem = it.saleItem.copy(quantity = newQuantity))
            } else it
        }
        _detailedSaleState.value = _detailedSaleState.value.copy(itemsWithProducts = updatedItems)
    }

    fun removeSaleItem(itemId: Int) {
        val updatedItems = _detailedSaleState.value.itemsWithProducts.filterNot {
            it.saleItem.id == itemId
        }
        _detailedSaleState.value = _detailedSaleState.value.copy(itemsWithProducts = updatedItems)
    }

    fun addSaleItem(productId: Int, quantity: Int, priceAtSale: Double) {
        viewModelScope.launch {
            val product = try {
                productRepository.getProductById(productId)
            } catch (e: Exception) {
                null
            }

            if (product != null) {
                val newItem = SaleItemWithProduct(
                    saleItem = SaleItem(
                        id = 0, // ID generado por la base de datos
                        saleId = saleId ?: 0,
                        productId = productId,
                        quantity = quantity,
                        priceAtSale = priceAtSale
                    ),
                    product = product
                )

                val updatedItems = _detailedSaleState.value.itemsWithProducts + newItem
                _detailedSaleState.value =
                    _detailedSaleState.value.copy(itemsWithProducts = updatedItems)
            }
        }
    }

    fun saveSale() {
        viewModelScope.launch {
            _savingState.value = SavingState.Saving
            try {
                val current = _detailedSaleState.value
                val saleToSave = current.sale?.copy(
                    items = current.itemsWithProducts.map { it.saleItem }
                )

                if (saleToSave != null) {
                    saleRepository.updateSale(saleToSave)
                    _savingState.value = SavingState.Success
                } else {
                    _savingState.value = SavingState.Error("Sale data is null.")
                }

            } catch (e: Exception) {
                _savingState.value = SavingState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun deleteSale() {
        viewModelScope.launch {
            _savingState.value = SavingState.Saving
            try {
                if (saleId != null) {
                    saleRepository.deleteSale(saleId)
                    _savingState.value = SavingState.Success
                } else {
                    _savingState.value = SavingState.Error("Sale ID not provided for deletion.")
                }
            } catch (e: Exception) {
                _savingState.value =
                    SavingState.Error(e.localizedMessage ?: "Failed to delete sale.")
            }
        }
    }
}
