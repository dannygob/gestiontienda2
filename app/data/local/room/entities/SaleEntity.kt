package com.your_app_name.data.local.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SaleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val clientId: Int,
    val saleDate: Long, // Or use String if you prefer
    val totalAmount: Double
)