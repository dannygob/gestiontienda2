package com.example.gestiontienda2.data.local.room.entities.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
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
    ],
    indices = [Index("sales")]
)
data class SaleItemsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val saleId: Long,
    val productId: Long,
    val quantity: Int,
    val unitPrice: Double,
)
