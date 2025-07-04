package com.example.gestiontienda2.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestiontienda2.domain.models.Purchase
import com.example.gestiontienda2.domain.models.PurchaseItem
import com.example.gestiontienda2.domain.models.TotalAmount
import com.example.gestiontienda2.domain.repository.PurchaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.nio.channels.spi.SelectorProvider
import java.util.TimeZone.LONG
import javax.inject.Inject

@HiltViewModel
class AddPurchaseViewModel @Inject constructor(
    private val purchaseRepository: PurchaseRepository // Inject your PurchaseRepository
) : ViewModel() {

    // State for the new purchase data
    private val _newPurchase = MutableStateFlow(
        Purchase(
            // Assuming 0 or similar for a new entry
            id1 = TODO(),// Placeholder, you'll need to handle provider selection
            id = TODO(), // Default to current time
            date = LONG, // Start with an empty list of items
            providerId = 0,
            totalAmount = 0.0,
            items = TODO(),
            total = this.total,
            purchaseDate = dateTimestamp = 0L, // Default to 0 or current time
            // Calculate based on items
            // Add other fields as needed
        )
    )
    val newPurchase: StateFlow<Purchase> = _newPurchase.asStateFlow()

    // State for saving process
    private val _totalAmount = MutableStateFlow<TotalAmount>(SavingState.Idle)
    val totalAmount: StateFlow<SavingState> = _totalAmount.asStateFlow()

    private val _selectedProvider = MutableStateFlow<SelectorProvider>(SavingState.Idle)
    val selectedProvider: StateFlow<SelectorProvider> = _selectedProvider.asStateFlow()

    private val _purchaseItems = MutableStateFlow<PurchaseItem>(SavingState.Idle)
    val purchaseItems: StateFlow<PurchaseItem> = _purchaseItems.asStateFlow()

    private val _savingState = MutableStateFlow<SavingState>(SavingState.Idle)
    val savingState: StateFlow<SavingState> = _savingState.asStateFlow()

    // Functions to update the new purchase data from UI input
    fun updateProvider(providerId: Int) {
        _newPurchase.value = _newPurchase.value.copy(providerId = providerId)
    }

    fun updatePurchaseDate(dateTimestamp: Long) {
        _newPurchase.value = _newPurchase.value.copy(purchaseDate = dateTimestamp)
    }

    fun addItem(item: PurchaseItem) {
        _newPurchase.value = _newPurchase.value.copy(items = _newPurchase.value.items + item)
        calculateTotalAmount()
    }

    fun updateItem(item: PurchaseItem) {
        _newPurchase.value = _newPurchase.value.copy(
            items = _newPurchase.value.items.map {
                if (it.id == item.id) item else it
            }
        )
        calculateTotalAmount()
    }

    fun removeItem(itemId: Int) {
        _newPurchase.value = _newPurchase.value.copy(
            items = _newPurchase.value.items.filterNot { it.id == itemId }
        )
        calculateTotalAmount()
    }

    // Helper function to calculate total amount
    private fun calculateTotalAmount() {
        val total = _newPurchase.value.items.sumOf { it.quantity * it.priceAtPurchase }
        _newPurchase.value = _newPurchase.value.copy(totalAmount = total)
    }

    // Function to save the new purchase
    fun savePurchase(selectedDateTimestamp: Long) {
        _savingState.value = SavingState.Saving
        viewModelScope.launch {
            try {
                // Call repository to insert the new purchase
                purchaseRepository.insertPurchase(_newPurchase.value) // Assuming insertPurchase takes a Purchase object
                _savingState.value = SavingState.Success
                // Optionally reset the newPurchase state after successful save
            } catch (e: Exception) {
                _savingState.value =
                    SavingState.Error("Failed to save purchase: ${e.localizedMessage}")
            }
        }
    }

    // Function to reset saving state, useful after showing an error or success message
    fun resetSavingState() {
        _savingState.value = SavingState.Idle
    }
}