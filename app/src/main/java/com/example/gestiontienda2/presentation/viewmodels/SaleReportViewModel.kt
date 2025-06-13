package com.example.gestiontienda2.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestiontienda2.data.repository.SaleWithItems
import com.example.gestiontienda2.domain.repository.SaleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaleReportViewModel @Inject constructor(
    private val saleRepository: SaleRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _startDate = savedStateHandle.getStateFlow<Long?>("startDate", null)
    val startDate: StateFlow<Long?> = _startDate

    private val _endDate = savedStateHandle.getStateFlow<Long?>("endDate", null)
    val endDate: StateFlow<Long?> = _endDate

    private val _salesList = MutableStateFlow<List<SaleWithItems>>(emptyList())
    val salesList: StateFlow<List<SaleWithItems>> = _salesList

    init {
        viewModelScope.launch {
            combine(_startDate, _endDate) { start, end -> Pair(start, end) }
                .collectLatest { (start, end) ->
                    saleRepository.getSalesByDateRange(start, end).collectLatest { sales ->
                        _salesList.value = sales
                    }
                }
        }
    }
}
