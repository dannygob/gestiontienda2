package com.example.gestiontienda2.domain.usecases

import com.example.gestiontienda2.domain.models.Product
import com.example.gestiontienda2.domain.repository.ProductRepository
import javax.inject.Inject

class UpdateProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(product: Product) {
        productRepository.updateProduct(product)
    }
}