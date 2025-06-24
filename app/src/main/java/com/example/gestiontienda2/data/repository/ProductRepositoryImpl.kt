package com.example.gestiontienda2.data.repository

import com.example.gestiontienda2.data.local.dao.ProductDao
import com.example.gestiontienda2.data.remote.api.OpenFoodFactsApiService
import com.example.gestiontienda2.domain.models.Product
import com.example.gestiontienda2.domain.repository.ProductRepository
import com.gestiontienda2.data.remote.firebase.ProductFirebaseDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.example.gestiontienda2.data.local.entities.toDomain as productEntityToDomain
import com.example.gestiontienda2.data.local.entities.toEntity as productDomainToEntity
import com.example.gestiontienda2.data.remote.firebase.toDomain as productFirebaseToDomain

class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val productFirebaseDataSource: ProductFirebaseDataSource,
    private val openFoodFactsApiService: OpenFoodFactsApiService,
) : ProductRepository {

    override suspend fun updateProductStockQuantity(productId: Long, newStock: Int) {
        withContext(Dispatchers.IO) {
            productDao.updateStockQuantity(productId, newStock)
        }
    }

    override suspend fun updateProductReservedStockQuantity(
        productId: Long,
        newReservedStock: Int,
    ) {
        withContext(Dispatchers.IO) {
            productDao.updateReservedStockQuantity(productId, newReservedStock)
        }
    }

    override fun getAllProducts(): Flow<List<Product>> = flow {
        try {
            val firebaseProducts = productFirebaseDataSource.getAllProducts()
            withContext(Dispatchers.IO) {
                productDao.insertAllProducts(firebaseProducts.map {
                    it.productFirebaseToDomain().productDomainToEntity()
                })
            }
            emit(firebaseProducts.map { it.productFirebaseToDomain() })
        } catch (e: Exception) {
            emit(productDao.getAllProducts().map { entities -> entities.map { it.toDomain() } })
        }
    }

    override suspend fun getProductById(id: Int): Product? {
        return try {
            productFirebaseDataSource.getProductById(id.toString())?.let {
                withContext(Dispatchers.IO) {
                    productDao.insertProduct(it.productFirebaseToDomain().productDomainToEntity())
                }
                it.productFirebaseToDomain()
            }
        } catch (_: Exception) {
            productDao.getProductById(id)?.productEntityToDomain()
        }
    }

    suspend fun getProductByBarcodeFromApi(barcode: String): Product? {
        return try {
            val response = openFoodFactsApiService.getProductByBarcode(barcode)
            response.body()?.toDomain()
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun insertProduct(product: Product) {
        withContext(Dispatchers.IO) {
            productDao.insertProduct(product.productDomainToEntity())
        }
        try {
            productFirebaseDataSource.addProduct(product.toFirebase())
        } catch (_: Exception) {
        }
    }

    override suspend fun updateProduct(product: Product) {
        withContext(Dispatchers.IO) {
            productDao.updateProduct(product.productDomainToEntity())
        }
        try {
            productFirebaseDataSource.updateProduct(product.toFirebase())
        } catch (_: Exception) {
        }
    }

    override suspend fun deleteProduct(product: Product) {
        withContext(Dispatchers.IO) {
            productDao.deleteProduct(product.productDomainToEntity())
        }
        try {
            productFirebaseDataSource.deleteProduct(product.id.toString())
        } catch (_: Exception) {
        }
    }
}
