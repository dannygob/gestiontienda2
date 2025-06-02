package com.gestiontienda2.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestiontienda2.domain.models.PurchaseWithItems
import com.gestiontienda2.domain.repository.PurchaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PurchaseReportViewModel @Inject constructor(
    private val purchaseRepository: PurchaseRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _startDate = MutableStateFlow<Long?>(null)
    val startDate: StateFlow<Long?> = _startDate.asStateFlow()

    private val _endDate = MutableStateFlow<Long?>(null)
    val endDate: StateFlow<Long?> = _endDate.asStateFlow()

    private val _purchasesList = MutableStateFlow<List<PurchaseWithItems>>(emptyList())
    val purchasesList: StateFlow<List<PurchaseWithItems>> = _purchasesList.asStateFlow()

    _startDate.value = savedStateHandle.get<Long?>("startDate")
    _endDate.value = savedStateHandle.get<Long?>("endDate")

    init {
        // Observe changes in start and end dates and fetch purchases
        combine(_startDate, _endDate) { startDate, endDate ->
            Pair(startDate, endDate)
        }.collectLatest { (startDate, endDate) ->
            viewModelScope.launch {
                purchaseRepository.getPurchasesByDateRange(startDate, endDate)
                    .collectLatest { purchases ->
                    }
            }
        }

        fun setDateRange(start: Long?, end: Long?) {
            _startDate.value = start
            _endDate.value = end
        }

        // Add functions to handle potential loading/error states if needed
    }