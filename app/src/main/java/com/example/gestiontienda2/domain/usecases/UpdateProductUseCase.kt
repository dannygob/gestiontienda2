package com.example.minimarket.domain.usecases

import com.example.minimarket.domain.models.Product
import com.example.minimarket.domain.repositories.ProductRepository
import javax.inject.Inject

class UpdateProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(product: Product) {
        productRepository.updateProduct(product)
    }
}