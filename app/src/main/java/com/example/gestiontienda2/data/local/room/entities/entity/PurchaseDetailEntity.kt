package com.example.gestiontienda2.data.local.room.entities.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "purchase_details")
data class PurchaseDetailEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderId: Int, // Assuming this is a foreign key to an Order entity
    val clientId: Int,
    val orderDate: Long, // Using Long for timestamp
    val status: String,
    val totalAmount: Double,
)