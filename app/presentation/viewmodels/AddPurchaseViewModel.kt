package com.your_app_name.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.gestiontienda2.domain.models.Purchase
import com.example.gestiontienda2.domain.models.PurchaseItem
import com.example.gestiontienda2.domain.repository.PurchaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SavingState {
    object Idle : SavingState()
    object Saving : SavingState()
    object Success : SavingState()
    data class Error(val message: String) : SavingState()
}

@HiltViewModel
class AddPurchaseViewModel @Inject constructor(
    private val purchaseRepository: PurchaseRepository
) : ViewModel() {

    private val _newPurchase = MutableStateFlow(
        Purchase(
            id = "0",
            providerId = "0",
            purchaseDate = System.currentTimeMillis(),
            items = emptyList(),
            totalAmount = 0.0
        )
    )
    val newPurchase: StateFlow<Purchase> = _newPurchase.asStateFlow()

    private val _savingState = MutableStateFlow<SavingState>(SavingState.Idle)
    val savingState: StateFlow<SavingState> = _savingState.asStateFlow()

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

    private fun calculateTotalAmount() {
        val total = _newPurchase.value.items.sumOf { it.quantity * it.price }
        _newPurchase.value = _newPurchase.value.copy(totalAmount = total)
    }

    fun savePurchase() {
        _savingState.value = SavingState.Saving
        viewModelScope.launch {
            try {
                purchaseRepository.insertPurchase(_newPurchase.value)
                _savingState.value = SavingState.Success
                _newPurchase.value = Purchase(
                    id = "0",
                    providerId = "0",
                     purchaseDate = System.currentTimeMillis(),
                     items = emptyList(),
                     totalAmount = 0.0
                )
            } catch (e: Exception) {
                _savingState.value = SavingState.Error("Failed to save purchase: ${e.localizedMessage}")
            }
        }
    }

    fun resetSavingState() {
        _savingState.value = SavingState.Idle
    }

    // TODO: 1. Investigate the UI layer to identify where SavingState.Idle is being incorrectly passed as a PurchaseItem.
    // TODO: 2. Ensure that the UI observes the correct StateFlow (SavingState) and handles the different states accordingly.
}
