package com.example.gestiontienda2.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.gestiontienda2.data.local.dao.ClientDao
import com.example.gestiontienda2.data.local.dao.ProductDao
import com.example.gestiontienda2.data.local.dao.SaleDao
import com.example.gestiontienda2.data.local.room.entities.SaleEntity
import com.example.gestiontienda2.data.local.room.entities.SaleItemEntity
import com.example.gestiontienda2.data.local.room.entities.SaleWithItems
import com.example.gestiontienda2.data.remote.firebase.datasource.SaleFirebaseDataSource
import com.example.gestiontienda2.domain.models.Sale
import com.example.gestiontienda2.domain.models.SaleItem
import com.gestiontienda2.domain.repository.SaleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class SaleRepositoryImpl @Inject constructor(
    private val context: Context,
    private val saleDao: SaleDao,
    private val saleFirebaseDataSource: SaleFirebaseDataSource,
    private val productDao: ProductDao,
    private val clientDao: ClientDao,
) : SaleRepository {

    override fun getAllSales(): Flow<List<Sale>> {
        return saleDao.getAllSalesWithItems().map { salesWithItemsList ->
            salesWithItemsList.map { it.toDomain(productDao) }
        }
    }

    override suspend fun getSaleById(saleId: Int): Sale? {
        return withContext(Dispatchers.IO) {
            saleDao.getSaleWithItemsById(saleId)?.toDomain(productDao)
        }
    }

    override suspend fun addSale(sale: Sale): Long {
        return withContext(Dispatchers.IO) {
            val newSaleId = UUID.randomUUID().toString() // Genera un ID único
            val saleId = saleDao.insertSale(sale.copy(id = newSaleId.toInt()).toSaleEntity())
            val saleItemEntities = sale.items.map { it.toSaleItemEntity(saleId.toInt()) }
            saleDao.insertSaleItems(saleItemEntities)

            try {
                saleFirebaseDataSource.addSale(sale.toFirebaseModel(saleId.toString()))
            } catch (_: Exception) {
            }

            saleId
        }
    }

    override suspend fun updateSale(sale: Sale) {
        withContext(Dispatchers.IO) {
            try {
                saleDao.updateSale(sale.toSaleEntity())
                saleDao.deleteSaleItemsForSale(sale.id)
                saleDao.insertSaleItems(sale.items.map { it.toSaleItemEntity(sale.id) })
                saleFirebaseDataSource.updateSale(sale.toFirebaseModel(sale.id.toString()))
            } catch (_: Exception) {
            }
        }
    }

    override suspend fun deleteSale(sale: Sale) {
        withContext(Dispatchers.IO) {
            try {
                saleDao.deleteSale(sale.toSaleEntity())
                saleDao.deleteSaleItemsForSale(sale.id)
                saleFirebaseDataSource.deleteSale(sale.id.toString())
            } catch (_: Exception) {
            }
        }
    }

    override fun getSalesByDateRange(startDate: Long?, endDate: Long?): Flow<List<Sale>> {
        return saleDao.getSalesByDateRangeWithItems(startDate, endDate).map { salesWithItemsList ->
            salesWithItemsList.map { it.toDomain(productDao) }
        }
    }

    override suspend fun getTotalSalesAmount(startDate: Long?, endDate: Long?): Double {
        return withContext(Dispatchers.IO) {
            saleDao.getTotalSalesAmount(startDate, endDate)
        }
    }

    private fun isOnline(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    // region Mappers
    private fun Sale.toSaleEntity(): SaleEntity {
        return SaleEntity(
            id = id,
            date = date,
            clientId = clientId,
            total = total
        )
    }

    private fun SaleWithItems.toDomain(productDao: ProductDao): Sale {
        val saleItems = items.mapNotNull { saleItemEntity ->
            val product = productDao.getProductById(saleItemEntity.productId)
            saleItemEntity.toDomain(product?.toDomain())
        }
        return Sale(
            id = saleEntity.id,
            date = saleEntity.date,
            clientId = saleEntity.clientId,
            total = saleEntity.total,
            items = saleItems
        )
    }

    private fun SaleItem.toSaleItemEntity(saleId: Int): SaleItemEntity {
        return SaleItemEntity(
            id = id,
            saleId = saleId,
            productId = productId,
            quantity = quantity,
            priceAtSale = unitPrice
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
