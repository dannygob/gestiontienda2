package com.example.gestiontienda2.data.mapper

import com.example.gestiontienda2.data.local.entities.entity.OrderItemEntity
import com.example.gestiontienda2.domain.models.OrderItem

fun OrderItemEntity.toDomain(): OrderItem {
    return OrderItem(
        id = this.id,
        orderId = this.orderId,
        productId = this.productId,
        quantity = this.quantity,
        priceAtOrder = this.priceAtOrder
    )
}