package com.example.gestiontienda2.domain.usecases

import com.gestiontienda2.domain.repository.ProductRepository
import javax.inject.Inject

class UpdateProductStockUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(productId: Int, newStock: Int) {
        // In a real application, you would likely fetch the product first,
        // update its stock, and then save it back.
        // This is a simplified example directly calling an update method
        // which would need to handle fetching internally or the repository
        // would have a specific updateStock method.
        // Assuming a simplified update mechanism in the repository for demonstration:
        // productRepository.updateProductStock(productId, newStock)

        // A more accurate approach might involve fetching the product first:
        val product = productRepository.getProductById(productId)
        product?.let {
            val updatedProduct = it.copy(stock = newStock)
            productRepository.updateProduct(updatedProduct)
        }
    }
}