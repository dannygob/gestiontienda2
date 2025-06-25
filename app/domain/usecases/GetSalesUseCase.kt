package com.example.gestiontienda2.domain.usecases

import com.your_app_name.domain.models.Sale
import com.your_app_name.domain.repository.SaleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSalesUseCase @Inject constructor(
    private val saleRepository: SaleRepository
) {
    operator fun invoke(): Flow<List<Sale>> {
        return saleRepository.getSales()
    }
}
