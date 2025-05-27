package com.your_app_name.domain.usecase

import com.your_app_name.domain.model.Purchase
import com.your_app_name.domain.model.PurchaseDetail
import com.your_app_name.domain.repository.PurchaseRepository
import javax.inject.Inject

class InsertPurchaseWithDetailsUseCase @Inject constructor(
    private val purchaseRepository: PurchaseRepository
) {
    operator fun invoke(purchase: Purchase, details: List<PurchaseDetail>): suspend fun Long {
        return purchaseRepository.insertPurchaseWithDetails(purchase, details)
    }
}