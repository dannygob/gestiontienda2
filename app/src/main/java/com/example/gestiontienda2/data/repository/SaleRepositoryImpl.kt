import com.example.gestiontienda2.data.local.room.entities.SaleEntity
import com.example.gestiontienda2.data.local.room.entities.SaleItemEntity
import com.example.gestiontienda2.data.local.room.entities.SaleWithItems
import com.example.gestiontienda2.data.local.dao.ClientDao
import com.example.gestiontienda2.data.local.dao.ProductDao
import com.example.gestiontienda2.data.local.dao.SaleDao
import com.gestiontienda2.data.local.dao.SaleDao
import com.example.gestiontienda2.data.remote.firebase.datasource.SaleFirebaseDataSource
import com.example.gestiontienda2.domain.models.Sale
import com.gestiontienda2.domain.models.Sale
import com.example.gestiontienda2.domain.models.SaleItem
import com.gestiontienda2.domain.repository.SaleRepository
import com.gestiontienda2.domain.repository.isOnline
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

git add .
package com.your_app_name.data .repository

import com.gestiontienda2.data.local.dao.SaleDao
import com.gestiontienda2.domain.models.Sale
import com.gestiontienda2.domain.repository.isOnline
import kotlinx.coroutines.flow.map

class SaleRepositoryImpl @Inject constructor(
    private val saleDao: SaleDao,
    private val saleFirebaseDataSource: SaleFirebaseDataSource,
    private val productDao: ProductDao, // To get product details for SaleItem mapping
    private val clientDao: ClientDao, // To get client details for Sale mapping
    private val ioDispatcher: CoroutineContext // Assuming you have an IO dispatcher provided
) : SaleRepository {

    override fun getAllSales(): Flow<List<Sale>> {
        // Implement data source selection logic: fetch from Firebase if online,
        // update Room, then return from Room. If offline, return from Room directly.
        // This is a simplified example; a real implementation would involve
        // more sophisticated synchronization.
        if (isOnline()) {
            // TODO: Fetch from Firebase, update Room
        }
        return saleDao.getAllSalesWithItems().map { salesWithItemsList ->
            salesWithItemsList.map { it.toDomain(productDao) }
        }
    }

    override suspend fun getSaleById(saleId: Int): Sale? {
        return withContext(ioDispatcher) {
            // Implement data source selection logic
            if (isOnline()) {
                // TODO: Fetch from Firebase if needed and update Room
            }
            saleDao.getSaleWithItemsById(saleId)?.toDomain(productDao)
        }
    }

    override suspend fun addSale(sale: Sale): Long {
        return withContext(ioDispatcher) {
            // Generate a unique ID for the new sale before inserting into Room
            // This ensures consistency if Firebase generates its own ID later
            val newSaleId =
                generateUniqueId() // Implement a method to generate a unique ID (e.g., UUID)

            val saleId = saleDao.insertSale(sale.copy(id = newSaleId).toSaleEntity())
            val saleItemEntities = sale.items.map { it.copy(saleId = newSaleId).toSaleItemEntity() }
            saleDao.insertSaleItems(saleItemEntities)

            if (isOnline()) {
                // TODO: Add to Firebase
                try {
                    saleFirebaseDataSource.addSale(sale.toFirebaseModel(saleId.toString()))
                } catch (e: Exception) {
                    // Handle Firebase add error (e.g., log, show message)
                }
            }
            saleId
        }
    }

    override suspend fun updateSale(sale: Sale) {
        withContext(ioDispatcher) {
            saleDao.updateSale(sale.toSaleEntity())
            // Depending on update strategy, you might delete existing items
            // and insert new ones or update individually.
            // For simplicity, let's assume delete and insert for now.
            saleDao.deleteSaleItemsForSale(sale.id)
            val saleItemEntities = sale.items.map { it.toSaleItemEntity(sale.id) }
            saleDao.insertSaleItems(saleItemEntities.map { it.copy(id = 0) }) // Assuming id is auto-generated on insert

            try {
                if (isOnline()) {
                    saleFirebaseDataSource.updateSale(sale.toFirebaseModel(sale.id.toString()))
                }
            } catch (e: Exception) {
            }
        }
    }

    override suspend fun deleteSale(sale: Sale) {
        withContext(ioDispatcher) {
            saleDao.deleteSale(sale.toSaleEntity())
            saleDao.deleteSaleItemsForSale(sale.id) // Delete associated items

            if (isOnline()) {
                try {
                    saleFirebaseDataSource.deleteSale(sale.id.toString())
                } catch (e: Exception) {
                    // Handle Firebase delete error
                }
            }
        }
    }

    override fun getSalesByDateRange(startDate: Long?, endDate: Long?): Flow<List<Sale>> {
        // Implement data source selection logic if needed
        return saleDao.getSalesByDateRangeWithItems(startDate, endDate).map { salesWithItemsList ->
            salesWithItemsList.map { it.toDomain(productDao) }
        }
    }

    override suspend fun getTotalSalesAmount(startDate: Long?, endDate: Long?): Double {
        // Need to modify SaleDao to accept startDate and endDate parameters
        // and filter the query accordingly.
        // Example SQL modification in SaleDao:
        // @Query("SELECT SUM(total) FROM sales WHERE (:startDate IS NULL OR date >= :startDate) AND (:endDate IS NULL OR date <= :endDate)")
        // suspend fun getTotalSalesAmount(startDate: Long?, endDate: Long?): Double?
        return withContext(ioDispatcher) {
            // Assuming the SaleDao method now accepts dates
            saleDao.getTotalSalesAmount(startDate, endDate)
        }
    }

    // region Mappers
    private fun Sale.toSaleEntity(): SaleEntity {
        return com.gestiontienda2.data.local.room.entities.SaleEntity(
            id = id,
            date = date,
            clientId = clientId,
            total = total
        )
    }

    private fun SaleWithItems.toDomain(productDao: ProductDao): Sale {
        // This mapping requires fetching product details for each SaleItem.
        // In a real app, you might optimize this or fetch product details separately in the ViewModel.
        val saleItems = items.mapNotNull { saleItemEntity ->
            val product =
                productDao.getProductById(saleItemEntity.productId) // Assuming sync access is okay here or refactor
            saleItemEntity.toDomain(product?.toDomain())
        }
        return Sale( // Ensure all fields are mapped
            id = id,
            date = date,
            clientId = saleEntity.clientId, // Assuming SaleWithItems has the SaleEntity
            total = saleEntity.total, // Assuming SaleWithItems has the SaleEntity
            items = saleItems
        )
    }

    private fun SaleItem.toSaleItemEntity(saleId: Int): SaleItemEntity {
        return SaleItemEntity(
            id = id,
            saleId = saleId,
            productId = productId,
            quantity = quantity,
            priceAtSale = unitPrice // Assuming unitPrice maps to priceAtSale
        )
    }

    private fun SaleItemEntity.toDomain(product: com.gestiontienda2.domain.models.Product? = null): SaleItem {
        return SaleItem(
            id = id,
            saleId = saleId,
            productId = productId,
            quantity = quantity,
            priceAtSale = priceAtSale,
            product = product
        )
    }
    // endregion
}