package com.your_app_name.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.your_app_name.domain.model.ServiceExpense // Replace with your actual domain model
import com.your_app_name.domain.repository.ServiceExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServiceExpenseReportViewModel @Inject constructor(
    private val serviceExpenseRepository: ServiceExpenseRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _startDate = MutableStateFlow<Long?>(null)
    val startDate: StateFlow<Long?> = _startDate.asStateFlow()
    private val _endDate = MutableStateFlow<Long?>(null)
    val endDate: StateFlow<Long?> = _endDate.asStateFlow()

    private val _serviceExpensesList = MutableStateFlow<List<ServiceExpense>>(emptyList())
    val serviceExpensesList: StateFlow<List<ServiceExpense>> = _serviceExpensesList.asStateFlow()

    init {

        // Observe changes in dates and fetch expenses
        combine(_startDate, _endDate) { startDate, endDate ->
            Pair(startDate, endDate)
        }.collectLatest { (startDate, endDate) ->
            serviceExpenseRepository.getServiceExpensesByDateRange(startDate, endDate).collectLatest {
                _serviceExpensesList.value = it
            }
        }
    }

    // Retrieve dates from saved state handle (navigation arguments)
    private val initialStartDate = savedStateHandle.get<Long?>("startDate")
    private val initialEndDate = savedStateHandle.get<Long?>("endDate")


    fun setDateRange(startDate: Long?, endDate: Long?) {
        _startDate.value = startDate
        _endDate.value = endDate
    }

    // You might add functions here to set the dates from the UI
    // fun setStartDate(date: Long) { _startDate.value = date }
    // fun setEndDate(date: Long) { _endDate.value = date }
}