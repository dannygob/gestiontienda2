package com.example.android.ministore.domain.usecases

import com.example.android.ministore.domain.model.Product
import com.example.android.ministore.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(productId: Int): Product {
        return productRepository.getProductById(productId) // Assuming getProductById exists in repository
    }
}