package com.your_app_name.domain.usecases

import com.your_app_name.domain.models.Sale
import com.your_app_name.domain.repository.SaleRepository
import javax.inject.Inject

class GetSaleByIdUseCase @Inject constructor(
    private val saleRepository: SaleRepository
) {
    suspend operator fun invoke(saleId: Int): Sale? {
        return saleRepository.getSaleById(saleId)
    }
}