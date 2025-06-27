package com.yourcompany.app.domain.usecases

import com.yourcompany.app.domain.models.Product
import com.yourcompany.app.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    operator fun invoke(): Flow<List<Product>> {
        return productRepository.getAllProducts()
    }
}