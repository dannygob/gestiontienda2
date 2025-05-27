package com.your_app_name.domain.usecase

import com.your_app_name.domain.models.Product
import com.your_app_name.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductByBarcodeUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(barcode: String): Product? {
        return productRepository.getProductByBarcodeFromApi(barcode)
    }
}