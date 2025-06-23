package com.example.gestiontienda2.data.mapper

import com.example.gestiontienda2.data.repository.OrderWithItems
import com.example.gestiontienda2.domain.models.Client
import com.example.gestiontienda2.domain.models.Order
import com.example.gestiontienda2.domain.models.Product

fun OrderWithItems.toDomain(clients: List<Client>, products: List<Product>): Order {
    clients.find { it.id.toLong() == this.order.clientId } // Puede ser null si no existe
    val productsMap = products.associateBy { it.id }
    return Order(
        id = this.order.id.toInt() as Int,
        clientId = this.order.clientId.toInt() as Int,
        orderDate = this.order.orderDate,
        status = this.order.status,
        totalAmount = this.order.totalAmount,
        items = this.items.map { itemEntity ->
            itemEntity.toDomain().copy(product = productsMap[itemEntity.productId])
        }
    )
}
