package com.yourappname.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

import com.yourappname.domain.models.PurchaseDetail

@Entity(
    tableName = "purchase_details",
    foreignKeys = [
        ForeignKey(
            entity = PurchaseEntity::class,
            parentColumns = ["id"],
            childColumns = ["purchaseId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PurchaseDetailEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val purchaseId: Int,
    val productId: Int,
    val quantity: Int,
    val unitPrice: Double
)

fun PurchaseDetailEntity.toDomain(): PurchaseDetail {
    return PurchaseDetail(
        id = id,
        purchaseId = purchaseId,
        productId = productId,
        quantity = quantity,
        unitPrice = unitPrice
    )
}