package com.your_app_name.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.gestiontienda2.domain.models.Purchase // Assuming Purchase model exists
import com.example.gestiontienda2.domain.models.PurchaseItem // Assuming PurchaseItem model exists
import com.example.gestiontienda2.domain.repository.PurchaseRepository // Assuming PurchaseRepository exists
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Define a simple SavingState sealed class if you don't have one
sealed class SavingState {
    object Idle : SavingState()
    object Saving : SavingState()
    object Success : SavingState()
    data class Error(val message: String) : SavingState()
}

@HiltViewModel
class AddPurchaseViewModel @Inject constructor(
    private val purchaseRepository: PurchaseRepository // Inject your PurchaseRepository
) : ViewModel() {

    // State for the new purchase data
    private val _newPurchase = MutableStateFlow(
        Purchase(
            id = "0", // Assuming 0 or similar for a new entry
            providerId = "0", // Placeholder, you'll need to handle provider selection
            purchaseDate = System.currentTimeMillis(), // Default to current time
            items = emptyList(), // Start with an empty list of items
            totalAmount = 0.0 // Calculate based on items
            // Add other fields as needed
        )
    )
    val newPurchase: StateFlow<Purchase> = _newPurchase.asStateFlow()

    // State for saving process
    private val _savingState = MutableStateFlow<SavingState>(SavingState.Idle)
    val savingState: StateFlow<SavingState> = _savingState.asStateFlow()

    // Functions to update the new purchase data from UI input
    fun updateProvider(providerId: String) {
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

    fun removeItem(itemId: String) {
        _newPurchase.value = _newPurchase.value.copy(
            items = _newPurchase.value.items.filterNot { it.id == itemId }
        )
        calculateTotalAmount()
    }

    // Helper function to calculate total amount
    private fun calculateTotalAmount() {
        val total = _newPurchase.value.items.sumOf { it.quantity * it.price }
        _newPurchase.value = _newPurchase.value.copy(totalAmount = total)
    }

    // Function to save the new purchase
    fun savePurchase() {
        _savingState.value = SavingState.Saving
        viewModelScope.launch {
            try {
                // Call repository to insert the new purchase
                purchaseRepository.insertPurchase(_newPurchase.value) // Assuming insertPurchase takes a Purchase object
                _savingState.value = SavingState.Success
                // Optionally reset the newPurchase state after successful save
                _newPurchase.value = Purchase(
                    id = "0",
                    providerId = "0",
                     purchaseDate = System.currentTimeMillis(),
                     items = emptyList(),
                     totalAmount = 0.0
                     // Add other fields as needed
                )
            } catch (e: Exception) {
                _savingState.value = SavingState.Error("Failed to save purchase: ${e.localizedMessage}")
            }
        }
    }

    // Function to reset saving state, useful after showing an error or success message
    fun resetSavingState() {
        _savingState.value = SavingState.Idle
    }

    // TODO: Investigate the UI layer to identify where SavingState.Idle is being incorrectly passed as a PurchaseItem.
}
