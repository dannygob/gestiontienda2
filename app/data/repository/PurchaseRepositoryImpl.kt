package com.your_app_name.data.repository

import com.your_app_name.data.local.dao.PurchaseDao
import com.your_app_name.data.local.entities.PurchaseDetailEntity
import com.your_app_name.data.local.entities.PurchaseEntity
import com.your_app_name.domain.models.Purchase
import com.your_app_name.domain.models.PurchaseItem
import com.your_app_name.domain.repository.PurchaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PurchaseRepositoryImpl @Inject constructor(
    private val purchaseDao: PurchaseDao
) : PurchaseRepository {

    override suspend fun savePurchase(purchase: Purchase) {
        val purchaseId = purchaseDao.insertPurchase(purchase.toEntity())
        val detailEntities = purchase.items.map { it.toDetailEntity(purchaseId.toInt()) }
        purchaseDao.insertPurchaseDetails(detailEntities)
    }

    override fun getAllPurchases(): Flow<List<Purchase>> {
        return purchaseDao.getAllPurchases().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getPurchaseById(id: Int): Flow<Purchase?> {
        return purchaseDao.getPurchaseById(id).map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun updatePurchase(purchase: Purchase) {
        purchaseDao.updatePurchase(purchase.toEntity())
    }

    override suspend fun deletePurchase(purchase: Purchase) {
        purchaseDao.deletePurchase(purchase.toEntity())
    }

    override suspend fun getTotalPurchaseAmount(startDate: Long?, endDate: Long?): Double {
        return purchaseDao.getTotalPurchaseAmount(startDate, endDate)
    }

    override fun getPurchasesByDateRange(startDate: Long?, endDate: Long?): Flow<List<Purchase>> {
        return purchaseDao.getPurchasesWithItemsByDateRange(startDate, endDate).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    //region Mappers
    private fun Purchase.toEntity(): PurchaseEntity {
        return PurchaseEntity(
            id = this.id,
            date = this.purchaseDate,
            providerId = this.providerId,
            total = this.totalAmount
        )
    }

    private fun PurchaseEntity.toDomain(): Purchase {
        return Purchase(
            id = this.id,
            purchaseDate = this.date,
            providerId = this.providerId,
            items = emptyList(), // Puedes cargar los detalles si es necesario
            totalAmount = this.total
        )
    }

    private fun PurchaseItem.toDetailEntity(purchaseId: Int): PurchaseDetailEntity {
        return PurchaseDetailEntity(
            id = this.id,
            purchaseId = purchaseId,
            productId = this.productId,
            quantity = this.quantity,
            unitPrice = this.price
        )
    }
    //endregion
}
