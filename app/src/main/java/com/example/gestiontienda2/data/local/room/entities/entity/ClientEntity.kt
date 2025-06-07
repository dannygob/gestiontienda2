package com.example.gestiontienda2.data.local.room.entities.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clients")
data class ClientEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val phone: String,
    val address: String,
    val email: String,
    val paymentPreference: String,
)