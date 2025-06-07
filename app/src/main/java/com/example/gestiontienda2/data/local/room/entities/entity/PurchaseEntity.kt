package com.example.gestiontienda2.data.local.room.entities.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "purchases")
data class PurchaseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val providerId: Long,
    val date: Long,
    val total: Double,
)