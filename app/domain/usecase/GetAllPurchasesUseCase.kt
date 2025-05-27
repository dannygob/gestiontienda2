package com.your_app_name.domain.usecase

import com.your_app_name.domain.model.Purchase
import com.your_app_name.domain.repository.PurchaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllPurchasesUseCase @Inject constructor(
    private val purchaseRepository: PurchaseRepository
) {
    operator fun invoke(): Flow<List<Purchase>> {
        return purchaseRepository.getAllPurchases()
    }
}