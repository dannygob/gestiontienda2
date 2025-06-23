package com.example.gestiontienda2.data.local.entities.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "stock_adjustments")
data class StockAdjustmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val productId: Long,
    val quantity: Int,
    val reason: String,
    val date: Date
)
