package com.example.gestiontienda2.domain.usecases

import com.example.gestiontienda2.domain.models.Sale
import com.gestiontienda2.domain.repository.SaleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSalesUseCase @Inject constructor(
    private val saleRepository: SaleRepository
) {
    operator fun invoke(): Flow<List<Sale>> {
        return saleRepository.getSales()
    }
}