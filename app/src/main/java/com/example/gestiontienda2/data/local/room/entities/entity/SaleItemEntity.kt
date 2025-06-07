package com.example.gestiontienda2.data.local.room.entities.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sale_items")
data class SaleItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val saleId: Long,
    val productId: Long,
    val quantity: Int,
    val unitPrice: Double,
)