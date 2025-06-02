package com.example.gestiontienda2.data.repository

import com.gestiontienda2.data.local.dao.ProductDao
import com.gestiontienda2.data.remote.api.OpenFoodFactsApiService
import com.gestiontienda2.data.remote.firebase.ProductFirebaseDataSource
import com.gestiontienda2.domain.models.Product
import com.gestiontienda2.domain.models.toDomain
import com.gestiontienda2.domain.models.toFirebase
import com.gestiontienda2.domain.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.gestiontienda2.data.local.entities.toDomain as productEntityToDomain
import com.gestiontienda2.data.local.entities.toEntity as productDomainToEntity
import com.gestiontienda2.data.remote.firebase.toDomain as productFirebaseToDomain

class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val productFirebaseDataSource: ProductFirebaseDataSource, // Assume this is implemented
    private val openFoodFactsApiService: OpenFoodFactsApiService // Assume this is implemented
) : ProductRepository {

    override suspend fun updateProductStockQuantity(productId: Long, newStock: Int) {
        productDao.updateStockQuantity(productId, newStock)
    }

    override suspend fun updateProductReservedStockQuantity(
        productId: Long,
        newReservedStock: Int
    ) {
        productDao.updateReservedStockQuantity(productId, newReservedStock)
    }

    override fun getAllProducts(): Flow<List<Product>> {
        // Try to fetch from Firebase first
        kotlinx.coroutines.CoroutineScope(Dispatchers.IO).launch {
            try {
                val firebaseProducts = productFirebaseDataSource.getAllProducts()
                // If successful, update the local database
                productDao.insertAllProducts(firebaseProducts.map {
                    it.productFirebaseToDomain().productDomainToEntity()
                })
            } catch (e: Exception) {
                // Handle Firebase fetch failure (e.g., log error, no network)
                e.printStackTrace() // For debugging
            }
        }
        // Always return the flow from the local database
        return productDao.getAllProducts().map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun getProductById(id: Int): Product? {
        // Example: Prioritize local database, then Firebase
        return productDao.getProductById(id)?.productEntityToDomain() ?: try {
            // Fetch from Firebase if not in local
            val firebaseProduct = productFirebaseDataSource.getProductById(id)
            if (firebaseProduct != null) {
                // Optionally update local database with the fetched Firebase product
                productDao.insertProduct(
                    firebaseProduct.productFirebaseToDomain().productDomainToEntity()
                )
                firebaseProduct.productFirebaseToDomain()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    // Example function for fetching product by barcode from API
    suspend fun getProductByBarcodeFromApi(barcode: String): Product? {
        return try {
            val response = openFoodFactsApiService.getProductByBarcode(barcode)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!.toDomain() // Assume mapping function exists
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun insertProduct(product: Product) {
        // Insert into local
        productDao.insertProduct(product.productDomainToEntity())
        // Try to insert into Firebase
        try {
            productFirebaseDataSource.addProduct(product.toFirebase())
        } catch (e: Exception) {
            // Handle Firebase insertion failure (e.g., log error, implement retry)
            e.printStackTrace()
        }
    }

    override suspend fun updateProduct(product: Product) {
        // Update in local
        productDao.updateProduct(product.productDomainToEntity())
        // Try to update in Firebase
        try {
            productFirebaseDataSource.updateProduct(product.toFirebase())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun deleteProduct(product: Product) {
        // Example: Delete from local and Firebase
        productDao.deleteProduct(product.productDomainToEntity())
        try {
            // Delete from Firebase
            productFirebaseDataSource.deleteProduct(product.id.toString())
        } catch (e: Exception) {
            // Handle Firebase deletion failure
        }
    }
}