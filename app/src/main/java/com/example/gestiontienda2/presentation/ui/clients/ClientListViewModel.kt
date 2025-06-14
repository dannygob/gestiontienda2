package com.example.gestiontienda2.presentation.ui.clients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestiontienda2.domain.models.Client
import com.example.gestiontienda2.domain.usecases.GetClientsUseCase
import com.example.gestiontienda2.presentation.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientListViewModel @Inject constructor(
    private val getClientsUseCase: GetClientsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Client>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Client>>> = _uiState.asStateFlow()

    init {
        loadClients()
    }

    fun retry() {
        loadClients()
    }

    private fun loadClients() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            getClientsUseCase()
                .catch { exception ->
                    _uiState.value = UiState.Error(
                        exception.localizedMessage ?: "Error cargando clientes"
                    )
                }
                .collect { clientList ->
                    _uiState.value = UiState.Success(clientList)
                }
        }
    }
}