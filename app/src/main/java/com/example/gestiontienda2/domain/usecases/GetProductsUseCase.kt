package com.gestiontiendas2.app.domain.usecases

import com.gestiontiendas2.app.domain.models.Product
import com.gestiontiendas2.app.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    operator fun invoke(): Flow<List<Product>> {
        return productRepository.getAllProducts()
    }
}