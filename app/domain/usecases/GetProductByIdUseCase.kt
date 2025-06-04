package com.your_app_name.domain.usecases

import com.your_app_name.domain.models.Product
import com.your_app_name.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(productId: Int): Product? {
        return productRepository.getProductById(productId) // Assuming getProductById exists in repository
    }
}
