package com.example.gestiontienda2.data.repository

import com.example.gestiontienda2.data.local.dao.PurchaseDao
import com.example.gestiontienda2.data.local.room.entities.entity.PurchaseDetailEntity
import com.example.gestiontienda2.data.local.room.entities.entity.PurchaseEntity
import com.example.gestiontienda2.domain.models.Purchase
import com.example.gestiontienda2.domain.models.PurchaseDetail
import com.example.gestiontienda2.domain.models.PurchaseItem
import com.example.gestiontienda2.domain.repository.PurchaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PurchaseRepositoryImpl @Inject constructor(
    private val purchaseDao: PurchaseDao
) : PurchaseRepository {

    override suspend fun insertPurchaseWithDetails(
        purchase: Purchase,
        details: List<PurchaseDetail>,
    ) {
        withContext(Dispatchers.IO) {
            try {
                val purchaseId = purchaseDao.insertPurchase(purchase.toEntity())
                val detailEntities = details.map { it.toEntity(purchaseId.toInt()) }
                purchaseDao.insertPurchaseDetails(detailEntities)
            } catch (e: Exception) {
                e.printStackTrace() // Manejo de errores, podría ser logueado
            }
        }
    }

    override fun getAllPurchases(): Flow<List<Purchase>> {
        return purchaseDao.getAllPurchases().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getPurchaseById(id: Int): Flow<Purchase?> {
        return purchaseDao.getPurchaseById(id).map { entity -> entity?.toDomain() }
    }

    override suspend fun updatePurchase(purchase: Purchase) {
        withContext(Dispatchers.IO) {
            try {
                purchaseDao.updatePurchase(purchase.toEntity())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun deletePurchase(purchase: Purchase) {
        withContext(Dispatchers.IO) {
            try {
                purchaseDao.deletePurchase(purchase.toEntity())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun getTotalPurchaseAmount(startDate: Long?, endDate: Long?): Double {
        return withContext(Dispatchers.IO) {
            purchaseDao.getTotalPurchaseAmount(startDate, endDate)
        }
    }

    override fun getPurchasesByDateRange(
        startDate: Long?,
        endDate: Long?,
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
            total = this.total,
            purchaseDate = TODO(),
            totalAmount = TODO()
        )
    }

    private fun PurchaseEntity.toDomain(): Purchase {
        return Purchase(
            id = this.id,
            date = this.date,
            providerId = this.providerId,
            total = this.total,
            totalAmount = TODO(),
            items = TODO()
        )
    }

    private fun PurchaseDetail.toEntity(purchaseId: Int): PurchaseDetailEntity {
        return PurchaseDetailEntity(
            id = this.id,
            purchaseId = purchaseId,
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
            unitPrice = this.unitPrice,
            total = TODO()
        )
    }
    //endregion
}
