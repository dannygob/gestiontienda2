package com.example.gestiontienda2.domain.usecases

import com.example.gestiontienda2.domain.models.Sale
import com.example.gestiontienda2.domain.repository.SaleRepository
import javax.inject.Inject

class GetSaleByIdUseCase @Inject constructor(
    private val saleRepository: SaleRepository
) {
    suspend operator fun invoke(saleId: Int): Sale? {
        return saleRepository.getSaleById(saleId)
    }
}