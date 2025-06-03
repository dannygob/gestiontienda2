package com.example.gestiontienda2.data.local.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PurchaseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val providerId: Int,
    val purchaseDate: Long, // Or String if preferred
    val totalAmount: Double
)