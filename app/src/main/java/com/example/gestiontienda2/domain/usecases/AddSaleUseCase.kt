package com.gestiontienda2.domain.usecases

import com.gestiontienda2.domain.models.Sale
import com.gestiontienda2.domain.repository.SaleRepository
import javax.inject.Inject

class AddSaleUseCase @Inject constructor(
    private val saleRepository: SaleRepository
) {
    suspend operator fun invoke(sale: Sale): Long {
        return saleRepository.addSale(sale)
    }
}