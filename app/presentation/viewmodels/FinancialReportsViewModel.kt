package com.your_app_name.presentation.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.your_app_name.domain.repository.SaleRepository
import com.your_app_name.domain.repository.PurchaseRepository
import com.your_app_name.domain.repository.ServiceExpenseRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest // Use collectLatest
import kotlinx.coroutines.flow.combine // Keep combine
import kotlinx.coroutines.flow.flow // Import flow
import kotlinx.coroutines.flow.flatMapLatest // Keep flatMapLatest
import com.your_app_name.domain.models.Sale
@HiltViewModel
class FinancialReportsViewModel @Inject constructor(
    private val saleRepository: SaleRepository,
    private val purchaseRepository: PurchaseRepository,
    private val serviceExpenseRepository: ServiceExpenseRepository
) : ViewModel() {

    // StateFlows for selected date range
    private val _startDate = MutableStateFlow<Long?>(null)
    val startDate: StateFlow<Long?> = _startDate.asStateFlow()
    private val _endDate = MutableStateFlow<Long?>(null)
    val endDate: StateFlow<Long?> = _endDate.asStateFlow()

    // Placeholder StateFlows for financial data
    private val _totalSales = MutableStateFlow(0.0)
    val totalSales: StateFlow<Double> = _totalSales.asStateFlow()

    private val _totalPurchases = MutableStateFlow(0.0)
    val totalPurchases: StateFlow<Double> = _totalPurchases.asStateFlow()

    private val _totalServiceExpenses = MutableStateFlow(0.0)
    val totalServiceExpenses: StateFlow<Double> = _totalServiceExpenses.asStateFlow()

    private val _netProfit = MutableStateFlow(0.0)
    val netProfit: StateFlow<Double> = _netProfit.asStateFlow()

    init {
        combine(_startDate, _endDate) { startDate, endDate ->
            Pair(startDate, endDate)
        }
        .collectLatest { (startDate, endDate) ->
            loadFinancialData(startDate, endDate)
        }.let { /* To avoid unused result warning */ }
    }

    // Function to load financial data within a date range (or all data if dates are null)
    fun loadFinancialData(startDate: Long?, endDate: Long?) {
        viewModelScope.launch {
            val totalSalesAmount = saleRepository.getTotalSalesAmount(startDate, endDate)
            _totalSales.value = totalSalesAmount
            val totalPurchasesAmount = purchaseRepository.getTotalPurchaseAmount() // Modify to accept dates
            _totalPurchases.value = totalPurchasesAmount
            val totalServiceExpensesAmount = serviceExpenseRepository.getTotalServiceExpenseAmount() // Modify to accept dates
            _totalServiceExpenses.value = totalServiceExpensesAmount
            _netProfit.value = totalSalesAmount - totalPurchasesAmount - totalServiceExpensesAmount
        }
    }
}