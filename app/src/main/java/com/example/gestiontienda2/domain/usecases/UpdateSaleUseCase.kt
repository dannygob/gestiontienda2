package com.example.gestiontienda2.domain.usecases

import com.example.gestiontienda2.domain.models.Sale
import com.example.gestiontienda2.domain.repository.SaleRepository
import javax.inject.Inject

class UpdateSaleUseCase @Inject constructor(
    private val saleRepository: SaleRepository
) {
    suspend operator fun invoke(sale: Sale) {
        saleRepository.updateSale(sale)
    }
}