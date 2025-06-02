package com.gestiontienda2.data.local.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class ClientEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val clientId: Int,
    val orderDate: Long, // Using Long for timestamp
    val status: String,
    val totalAmount: Double
)