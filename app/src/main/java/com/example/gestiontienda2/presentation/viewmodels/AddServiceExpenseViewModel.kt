package com.example.gestiontienda2.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestiontienda2.domain.models.ServiceExpense
import com.example.gestiontienda2.domain.repository.ServiceExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private val AddServiceExpenseViewModel.newServiceExpense: ServiceExpense

@HiltViewModel
class AddServiceExpenseViewModel @Inject constructor(
    private val serviceExpenseRepository: ServiceExpenseRepository
) : ViewModel() {

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description.asStateFlow()

    private val _date = MutableStateFlow(System.currentTimeMillis()) // Default to current time
    val date: StateFlow<Long> = _date.asStateFlow()

    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> = _amount.asStateFlow()

    private val _category = MutableStateFlow("")
    val category: StateFlow<String> = _category.asStateFlow()

    private val _notes = MutableStateFlow("")
    val notes: StateFlow<String> = _notes.asStateFlow()

    private val _savingState = MutableStateFlow<SavingState>(SavingState.Idle)
    val savingState: StateFlow<SavingState> = _savingState.asStateFlow()

    fun updateDescription(newDescription: String) {
        _description.value = newDescription
    }

    fun updateDate(newDate: Long) {
        _date.value = newDate
    }

    fun updateAmount(newAmount: String) {
        _amount.value = newAmount
    }

    fun updateCategory(newCategory: String) {
        _category.value = newCategory
    }

    fun updateNotes(newNotes: String) {
        _notes.value = newNotes
    }

    fun saveServiceExpense() {
        _savingState.value = SavingState.Saving

        // Basic validation (can be expanded)
        val amountDouble = _amount.value.toDoubleOrNull()
        if (_description.value.isBlank() || amountDouble == null) {
            _savingState.value = SavingState.Error("Description and Amount are required.")
            return
        }

        viewModelScope.launch {
            try {
                serviceExpenseRepository.insertServiceExpense(newServiceExpense)
                _savingState.value = SavingState.Success
            } catch (e: Exception) {
                _savingState.value =
                    SavingState.Error("Failed to save service expense: ${e.localizedMessage}")
            }
        }
    }
}

// Define a simple SavingState sealed class (you might have this defined elsewhere)
// If you already have this, you can remove this definition.
sealed class SavingState {
    object Idle : SavingState()
    object Saving : SavingState()
    object Success : SavingState()
    data class Error(val message: String) : SavingState()
}