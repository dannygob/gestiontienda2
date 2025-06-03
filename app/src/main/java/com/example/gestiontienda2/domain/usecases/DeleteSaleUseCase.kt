package com.example.gestiontienda2.domain.usecases

import com.example.gestiontienda2.domain.models.Sale
import com.gestiontienda2.domain.repository.SaleRepository
import javax.inject.Inject

class DeleteSaleUseCase @Inject constructor(
    private val saleRepository: SaleRepository
) {
    suspend operator fun invoke(sale: Sale) {
        saleRepository.deleteSale(sale)
    }
}