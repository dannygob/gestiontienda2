package com.example.gestiontienda2.data.local.entities.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sale_details")
data class SaleDetailEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val clientId: Int,
    val orderId: Int, // Assuming this is a foreign key to an Order entity
    val orderDate: Long, // Using Long for timestamp
    val status: String,
    val totalAmount: Double,
)