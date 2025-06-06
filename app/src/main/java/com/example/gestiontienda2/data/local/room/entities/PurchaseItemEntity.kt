package com.example.gestiontienda2.data.local.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "purchase_items")
data class PurchaseItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val purchaseId: Long,
    val productId: Long,
    val quantity: Int,
    val unitPrice: Double,
)
