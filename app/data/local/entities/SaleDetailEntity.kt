package com.your_app_name.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

import com.your_app_name.domain.models.SaleDetail

@Entity(
    tableName = "sale_details",
    foreignKeys = [
        ForeignKey(
            entity = SaleEntity::class,
            parentColumns = ["id"],
            childColumns = ["saleId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SaleDetailEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val saleId: Int,
    val productId: Int,
    val quantity: Int,
    val unitPrice: Double
)

fun SaleDetailEntity.toDomain(): SaleDetail {
    return SaleDetail(
        id = id,
        saleId = saleId,
        productId = productId,
        quantity = quantity,
        unitPrice = unitPrice
    )
}

fun SaleDetail.toEntity(): SaleDetailEntity {
    return SaleDetailEntity(
        id = id,
        saleId = saleId,
        productId = productId,
        quantity = quantity,
        unitPrice = unitPrice
    )
}