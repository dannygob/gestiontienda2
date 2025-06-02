package com.gestiontienda2.data.local.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = PurchaseEntity::class,
        parentColumns = ["id"],
        childColumns = ["purchaseId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class PurchaseItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val purchaseId: Int,
    val productId: Int,
    val quantity: Int,
    val purchasePrice: Double
)