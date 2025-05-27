package com.your_app_name.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.your_app_name.domain.models.Purchase

@Entity(tableName = "purchases")
data class PurchaseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: Long,
    val providerId: Int?, // Make nullable to match domain model if needed
    val total: Double
)

fun PurchaseEntity.toDomainModel(): Purchase {
    return Purchase(
        id = id,
        date = date,
        providerId = providerId,
        total = total
    )
}

fun Purchase.toEntity(): PurchaseEntity {
    return PurchaseEntity(
        id = id, // Assuming ID from domain model is used for updates
        date = date,
        providerId = providerId,
        total = total
    )
}