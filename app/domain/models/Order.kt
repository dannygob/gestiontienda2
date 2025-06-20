package com.yourcompany.yourappname.domain.models

/**
 * Representa un pedido realizado por un cliente.
 *
 * @property id Identificador único del pedido.
 * @property clientId ID del cliente que realizó el pedido.
 * @property orderDate Fecha del pedido en formato timestamp.
 * @property status Estado actual del pedido (por ejemplo: PENDING, COMPLETED, CANCELLED).
 * @property totalAmount Monto total del pedido calculado a partir de los ítems.
 * @property items Lista de productos incluidos en el pedido.
 */
data class Order(
    val id: Int = 0,
    val clientId: Int,
    val orderDate: Long,
    val status: OrderStatus,
    val totalAmount: Double,
    val items: List<OrderItem> = emptyList()
)
