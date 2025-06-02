package com.your_app_name.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.your_app_name.domain.models.Client
import com.your_app_name.domain.models.Sale
import com.your_app_name.domain.repository.ClientRepository
import com.your_app_name.domain.repository.SaleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaleListViewModel @Inject constructor(
    private val saleRepository: SaleRepository
    private val clientRepository: ClientRepository // Inject ClientRepository
) : ViewModel() {

    // Data class to combine Sale and Client for the UI
    data class SaleWithClient(
        val sale: Sale,
        val client: Client?
    )

    private val _salesState = MutableStateFlow(SaleListState())
    val salesState: StateFlow<SaleListState> = _salesState.asStateFlow()

    init {
        loadSales()
    }

    private fun loadSales() {
        viewModelScope.launch {
            _salesState.value = SaleListState(isLoading = true)
            try {
                saleRepository.getAllSales().collectLatest { salesList ->
                    // Fetch client for each sale
                    val salesWithClients = salesList.map { sale ->
                        val client = try {
                            clientRepository.getClientById(sale.clientId) // Assuming getClientById exists
                        } catch (e: Exception) {
                            null // Or a default "Unknown Client" object
                        }
                        SaleWithClient(sale = sale, client = client)
                    }
                    _salesState.value =
                        SaleListState(salesWithClients = salesWithClients) // Update state with SaleWithClient list
                }
            } catch (e: Exception) {
                _salesState.value = SaleListState(error = e.localizedMessage ?: "Unknown error")
            }
        }
    }
}

data class SaleListState(
    val isLoading: Boolean = false,
    val salesWithClients: List<SaleWithClient> = emptyList(), // Use SaleWithClient
    val error: String? = null
)