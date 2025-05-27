package com.your_app_name.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

import com.your_app_name.domain.models.ServiceExpense
@Entity(tableName = "service_expenses")
data class ServiceExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: String,
    val amount: Double,
    val date: Long,
    val description: String
)

fun ServiceExpenseEntity.toDomainModel(): ServiceExpense {
    return ServiceExpense(
        id = id,
        type = type,
        amount = amount,
        date = date,
        description = description
    )
}

fun ServiceExpense.toEntity(): ServiceExpenseEntity {
    return ServiceExpenseEntity(
        id = id,
        type = type,
        amount = amount,
        date = date,
        description = description
    )
}