package com.your_app_name.data.local.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "order_items",
    foreignKeys = [ForeignKey(
        entity = OrderEntity::class,
        parentColumns = ["id"],
        childColumns = ["orderId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("orderId"), Index("productId")]
)
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderId: Int,
    val productId: Int,
    val quantity: Int,
    val priceAtOrder: Double
)