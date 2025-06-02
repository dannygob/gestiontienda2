package com.your_app_name.data.local.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "sale_items",
    foreignKeys = [
        ForeignKey(
            entity = SaleEntity::class,
            parentColumns = ["id"],
            childColumns = ["saleId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SaleItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val saleId: Int,
    val productId: Int,
    val quantity: Int,
    val priceAtSale: Double
)