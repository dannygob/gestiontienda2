package com.example.gestiontienda2.domain.usecases

import com.example.gestiontienda2.domain.models.Product
import com.your_app_name.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(productId: Int): Product? {
        return productRepository.getProductById(productId) // Assuming getProductById exists in repository
    }
}
