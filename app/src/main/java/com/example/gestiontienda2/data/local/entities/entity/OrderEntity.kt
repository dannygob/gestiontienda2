package com.example.gestiontienda2.data.local.entities.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderId: Int, // Assuming this is a foreign key to an Order entity
    val clientId: Long,
    val orderDate: Long, // Using Long for timestamp
    val status: String,
    val totalAmount: Double,
)