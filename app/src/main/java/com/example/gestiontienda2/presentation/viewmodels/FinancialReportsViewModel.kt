package com.example.gestiontienda2.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestiontienda2.domain.repository.PurchaseRepository
import com.example.gestiontienda2.domain.repository.SaleRepository
import com.example.gestiontienda2.domain.repository.ServiceExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FinancialReportsViewModel @Inject constructor(
    private val saleRepository: SaleRepository,
    private val purchaseRepository: PurchaseRepository,
    private val serviceExpenseRepository: ServiceExpenseRepository
) : ViewModel() {

    private val _startDate = MutableStateFlow<Long?>(null)
    val startDate: StateFlow<Long?> = _startDate.asStateFlow()

    private val _endDate = MutableStateFlow<Long?>(null)
    val endDate: StateFlow<Long?> = _endDate.asStateFlow()

    private val _totalSales = MutableStateFlow(0.0)
    val totalSales: StateFlow<Double> = _totalSales.asStateFlow()

    private val _totalPurchases = MutableStateFlow(0.0)
    val totalPurchases: StateFlow<Double> = _totalPurchases.asStateFlow()

    private val _totalServiceExpenses = MutableStateFlow(0.0)
    val totalServiceExpenses: StateFlow<Double> = _totalServiceExpenses.asStateFlow()

    private val _netProfit = MutableStateFlow(0.0)
    val netProfit: StateFlow<Double> = _netProfit.asStateFlow()

    init {
        viewModelScope.launch {
            combine(_startDate, _endDate) { startDate, endDate ->
                Pair(startDate, endDate)
            }.collect { (start, end) ->
                loadFinancialData(start, end)
            }
        }
    }

    fun loadFinancialData(startDate: Long?, endDate: Long?) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val totalSalesAmount = saleRepository.getTotalSalesAmount(startDate, endDate)
                val totalPurchasesAmount =
                    purchaseRepository.getTotalPurchaseAmount(startDate, endDate)
                val totalServiceExpensesAmount =
                    serviceExpenseRepository.getTotalServiceExpenseAmount(startDate, endDate)

                _totalSales.value = totalSalesAmount
                _totalPurchases.value = totalPurchasesAmount
                _totalServiceExpenses.value = totalServiceExpensesAmount
                _netProfit.value =
                    totalSalesAmount - totalPurchasesAmount - totalServiceExpensesAmount
            }
        }
    }
}
