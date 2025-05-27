package com.your_app_name.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.your_app_name.domain.models.Order

@Entity(
    tableName = "orders",
    foreignKeys = [
        ForeignKey(
            entity = ClientEntity::class,
            parentColumns = ["id"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val clientId: Int,
    val date: Long,
    val state: String,
    val total: Double
)

fun OrderEntity.toDomainModel(): Order {
    return Order(
        id = id,
        clientId = clientId,
        date = date,
        state = state,
        total = total
    )
}

fun Order.toEntity(): OrderEntity {
    return OrderEntity(
        id = id,
        clientId = clientId,
        date = date,
        state = state,
        total = total
    )
}