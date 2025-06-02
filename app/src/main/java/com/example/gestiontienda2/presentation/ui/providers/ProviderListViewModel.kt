package com.gestiontienda2.presentation.ui.providers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestiontienda2.domain.models.Provider
import com.gestiontienda2.domain.usecases.GetProvidersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProviderListViewModel @Inject constructor(
    private val getProvidersUseCase: GetProvidersUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _providers = MutableStateFlow<List<Provider>>(emptyList())
    val providers: StateFlow<List<Provider>> = _providers.asStateFlow()

    init {
        loadProviders()
    }

    private fun loadProviders() {
        viewModelScope.launch {
            getProvidersUseCase()
                .onStart { _isLoading.value = true }
                .catch { e ->
                    _errorMessage.value = "Error loading providers: ${e.message}"
                    _isLoading.value = false
                }
                .collect { providerList ->
                    _providers.value = providerList
                    _isLoading.value = false
                }
        }
    }
}