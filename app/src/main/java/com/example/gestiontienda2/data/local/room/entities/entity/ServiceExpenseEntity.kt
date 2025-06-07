package com.example.gestiontienda2.data.local.room.entities.entity

import android.R.string
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "service_expenses")
data class ServiceExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val description: String,
    val date: string, // Or String, depending on how you store dates
    val amount: Double,
    val category: String,
    val notes: String?,
    val type: String, // e.g., "service", "expense",

)