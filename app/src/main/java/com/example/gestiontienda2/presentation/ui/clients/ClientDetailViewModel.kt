package com.your_app_name.presentation.ui.clients

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.your_app_name.domain.models.Client
import com.your_app_name.domain.usecases.GetClientByIdUseCase
import com.your_app_name.domain.usecases.UpdateClientUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getClientByIdUseCase: GetClientByIdUseCase,
    private val updateClientUseCase: UpdateClientUseCase // You'll create this UseCase
) : ViewModel() {

    private val clientId: Int = checkNotNull(savedStateHandle["clientId"])

    private val _client = MutableStateFlow<Client?>(null)
    val client: StateFlow<Client?> = _client.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _editMode = MutableStateFlow(false)
    val editMode: StateFlow<Boolean> = _editMode.asStateFlow()

    private val _savingState = MutableStateFlow<SavingState>(SavingState.Idle)
    val savingState: StateFlow<SavingState> = _savingState.asStateFlow()

    init {
        loadClient()
    }

    private fun loadClient() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val fetchedClient = getClientByIdUseCase(clientId)
                _client.value = fetchedClient
            } catch (e: Exception) {
                _errorMessage.value = "Error loading client: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleEditMode() {
        _editMode.value = !_editMode.value
    }

    fun updateClientDetails(updatedClient: Client) {
        _client.value = updatedClient
    }

    fun saveClient() {
        val currentClient = _client.value ?: return
        viewModelScope.launch {
            _savingState.value = SavingState.Saving
            try {
                updateClientUseCase(currentClient)
                _savingState.value = SavingState.Success
                _editMode.value = false // Exit edit mode on successful save
            } catch (e: Exception) {
                _errorMessage.value = "Error saving client: ${e.message}"
                _savingState.value = SavingState.Error
            }
        }
    }
}

sealed class SavingState {
    object Idle : SavingState()
    object Saving : SavingState()
    object Success : SavingState()
    object Error : SavingState()
}