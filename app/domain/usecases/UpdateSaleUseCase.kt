package com.example.gestiontienda2.domain.usecases

import com.your_app_name.domain.models.Sale
import com.your_app_name.domain.repository.SaleRepository
import javax.inject.Inject

class UpdateSaleUseCase @Inject constructor(
    private val saleRepository: SaleRepository
) {
    suspend operator fun invoke(sale: Sale) {
        saleRepository.updateSale(sale)
    }
}
