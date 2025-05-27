package com.your_app_name.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.your_app_name.domain.models.Sale

@Entity(tableName = "sales")
data class SaleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: Long,
    val clientId: Int,
    val total: Double
)

fun SaleEntity.toDomainModel(): Sale {
    return Sale(
        id = id,
        date = date,
        clientId = clientId,
        total = total
    )
}
