package com.example.gestiontienda2.data.remote.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sales")
data class SaleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val clientId: Long,
    val date: Long,
    val total: Double,
)
