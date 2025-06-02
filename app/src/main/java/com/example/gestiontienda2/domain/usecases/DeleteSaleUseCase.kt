package com.your_app_name.domain.usecases

import com.your_app_name.domain.models.Sale
import com.your_app_name.domain.repository.SaleRepository
import javax.inject.Inject

class DeleteSaleUseCase @Inject constructor(
    private val saleRepository: SaleRepository
) {
    suspend operator fun invoke(sale: Sale) {
        saleRepository.deleteSale(sale)
    }
}