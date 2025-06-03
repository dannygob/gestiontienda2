package com.gestiontienda2.presentation.ui.providers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestiontienda2.domain.models.Provider
import com.gestiontienda2.domain.usecases.GetProviderByIdUseCase
import com.example.gestiontienda2.domain.usecases.UpdateProviderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProviderDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getProviderByIdUseCase: GetProviderByIdUseCase,
    private val updateProviderUseCase: UpdateProviderUseCase // Inject the update use case
) : ViewModel() {

    private val providerId: Int =
        savedStateHandle["providerId"] ?: -1 // Get provider ID from SavedStateHandle

    private val _provider = MutableStateFlow<Provider?>(null)
    val provider: StateFlow<Provider?> = _provider.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _editMode = MutableStateFlow(false)
    val editMode: StateFlow<Boolean> = _editMode.asStateFlow()

    private val _savingState = MutableStateFlow<SavingState>(SavingState.Idle)
    val savingState: StateFlow<SavingState> = _savingState.asStateFlow()

    // State for editable fields (in edit mode)
    private val _editableName = MutableStateFlow("")
    val editableName: StateFlow<String> = _editableName.asStateFlow()

    private val _editablePhone = MutableStateFlow("")
    val editablePhone: StateFlow<String> = _editablePhone.asStateFlow()

    private val _editableEmail = MutableStateFlow("")
    val editableEmail: StateFlow<String> = _editableEmail.asStateFlow()

    private val _editableAddress = MutableStateFlow("")
    val editableAddress: StateFlow<String> = _editableAddress.asStateFlow()


    init {
        if (providerId != -1) {
            loadProvider(providerId)
        } else {
            _errorMessage.value = "Invalid Provider ID"
        }
    }

    private fun loadProvider(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val loadedProvider = getProviderByIdUseCase(id)
                _provider.value = loadedProvider
                if (loadedProvider != null) {
                    _editableName.value = loadedProvider.name
                    _editablePhone.value = loadedProvider.phone ?: ""
                    _editableEmail.value = loadedProvider.email ?: ""
                    _editableAddress.value = loadedProvider.address ?: ""
                } else {
                    _errorMessage.value = "Provider not found"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error loading provider: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleEditMode() {
        _editMode.value = !_editMode.value
        // Reset editable fields if exiting edit mode without saving
        if (!_editMode.value && _provider.value != null) {
            _editableName.value = _provider.value!!.name
            _editablePhone.value = _provider.value!!.phone ?: ""
            _editableEmail.value = _provider.value!!.email ?: ""
            _editableAddress.value = _provider.value!!.address ?: ""
        }
    }

    fun updateName(name: String) {
        _editableName.value = name
    }

    fun updatePhone(phone: String) {
        _editablePhone.value = phone
    }

    fun updateEmail(email: String) {
        _editableEmail.value = email
    }

    fun updateAddress(address: String) {
        _editableAddress.value = address
    }

    fun saveProvider() {
        val currentProvider = _provider.value ?: return // Cannot save if provider is null

        // Create an updated Provider object from editable fields
        val updatedProvider = currentProvider.copy(
            name = _editableName.value,
            phone = if (_editablePhone.value.isNotBlank()) _editablePhone.value else null,
            email = if (_editableEmail.value.isNotBlank()) _editableEmail.value else null,
            address = if (_editableAddress.value.isNotBlank()) _editableAddress.value else null
        )

        viewModelScope.launch {
            _savingState.value = SavingState.Saving
            try {
                updateProviderUseCase(updatedProvider)
                _savingState.value = SavingState.Success
                _provider.value =
                    updatedProvider // Update the displayed provider after successful save
                _editMode.value = false // Exit edit mode after saving
            } catch (e: Exception) {
                _savingState.value = SavingState.Error("Error saving provider: ${e.message}")
            }
        }
    }

    sealed class SavingState {
        object Idle : SavingState()
        object Saving : SavingState()
        object Success : SavingState()
        data class Error(val message: String) : SavingState()
    }
}