package com.example.gestiontienda2.domain.usecases

import com.example.gestiontienda2.domain.models.Sale
import com.example.gestiontienda2.domain.models.SaleDetail
import com.gestiontienda2.domain.repository.ProductRepository
import com.gestiontienda2.domain.repository.SaleRepository
import javax.inject.Inject

class CreateSaleUseCase @Inject constructor(
    private val saleRepository: SaleRepository,
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(sale: Sale, saleDetails: List<SaleDetail>) {
        // Insert the sale and its details
        saleRepository.insertSaleWithDetails(sale, saleDetails)

        // Update product stock
        saleDetails.forEach { detail ->
            // You would typically get the current product first to calculate the new stock
            // For simplicity here, we'll assume an update function exists that subtracts
            // the sold quantity from the current stock.
            // A more robust implementation would fetch the product, calculate new stock, then update.
            productRepository.updateProductStock(
                detail.productId,
                detail.quantity
            ) // This method needs to be defined in ProductRepository
        }
    }
}