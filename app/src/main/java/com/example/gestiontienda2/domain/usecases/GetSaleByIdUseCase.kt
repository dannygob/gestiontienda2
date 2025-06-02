package com.gestiontienda2.domain.usecases

import com.gestiontienda2.domain.models.Sale
import com.gestiontienda2.domain.repository.SaleRepository
import javax.inject.Inject

class GetSaleByIdUseCase @Inject constructor(
    private val saleRepository: SaleRepository
) {
    suspend operator fun invoke(saleId: Int): Sale? {
        return saleRepository.getSaleById(saleId)
    }
}