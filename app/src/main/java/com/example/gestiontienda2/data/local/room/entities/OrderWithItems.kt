package com.example.gestiontienda2.data.local.room.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.example.gestiontienda2.data.local.room.entities.OrderEntity
import com.example.gestiontienda2.data.local.room.entities.OrderItemEntity

data class OrderWithItems(
    @Embedded val order: OrderEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "orderId"
    )
    val items: List<OrderItemEntity>
)