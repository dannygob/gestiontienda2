package com.example.gestiontienda2.data.repository

import com.example.gestiontienda2.data.local.dao.PurchaseDao
import com.example.gestiontienda2.data.local.room.entities.PurchaseDetailEntity
import com.example.gestiontienda2.data.local.room.entities.PurchaseEntity
import com.gestiontienda2.data.local.entities.PurchaseDetailEntity
import com.gestiontienda2.data.local.entities.PurchaseEntity
import com.example.gestiontienda2.domain.models.Purchase
import com.example.gestiontienda2.domain.models.PurchaseDetail
import com.example.gestiontienda2.domain.models.PurchaseItem
import com.example.gestiontienda2.domain.repository.PurchaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PurchaseRepositoryImpl @Inject constructor(
    private val purchaseDao: PurchaseDao
) : PurchaseRepository {

    override suspend fun insertPurchaseWithDetails(
        purchase: Purchase,
        details: List<PurchaseDetail>
    ) {
        val purchaseId = purchaseDao.insertPurchase(purchase.toEntity())
        val detailEntities = details.map { it.toEntity(purchaseId.toInt()) }
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

    override fun getPurchasesByDateRange(
        startDate: Long?,
        endDate: Long?
    ): Flow<List<PurchaseItem>> {
        return purchaseDao.getPurchasesItemByDateRange(startDate, endDate).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    //region Mappers
    private fun Purchase.toEntity(): PurchaseEntity {
        return PurchaseEntity(
            id = this.id,
            date = this.date,
            providerId = this.providerId,
            total = this.total
        )
    }

    private fun PurchaseEntity.toDomain(): Purchase {
        return Purchase(
            id = this.id,
            date = this.date,
            providerId = this.providerId,
            total = this.total
        )
    }

    private fun PurchaseDetail.toEntity(purchaseId: Int): PurchaseDetailEntity {
        return PurchaseDetailEntity(
            id = this.id,
            purchaseId = purchaseId, // Use the generated purchaseId
            productId = this.productId,
            quantity = this.quantity,
            unitPrice = this.unitPrice
        )
    }

    private fun PurchaseDetailEntity.toDomain(): PurchaseDetail {
        return PurchaseDetail(
            id = this.id,
            purchaseId = this.purchaseId,
            productId = this.productId,
            quantity = this.quantity,
            unitPrice = this.unitPrice
        )
    }
    //endregion
}