package com.example.gestiontienda2.data.local.entities.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "service_expenses")
data class ServiceExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val description: String,
    val date: Unit, // Or String, depending on how you store dates
    val amount: String, // Or Double, depending on how you store amounts
    val category: Int,
    val notes: String?,
    val type: Int, // e.g., "service", "expense",

)