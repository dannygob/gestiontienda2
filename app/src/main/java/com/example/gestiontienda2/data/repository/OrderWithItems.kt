package com.example.gestiontienda2.data.repository

import androidx.room.Embedded
import androidx.room.Relation
import com.example.gestiontienda2.data.local.room.entities.entity.OrderEntity
import com.example.gestiontienda2.data.local.room.entities.entity.OrderItemEntity

data class OrderWithItems(
    @Embedded val order: OrderEntity,
    @Relation(

        parentColumn = "id",
        entityColumn = "orderId"
    )
    val items: List<OrderItemEntity>,
)