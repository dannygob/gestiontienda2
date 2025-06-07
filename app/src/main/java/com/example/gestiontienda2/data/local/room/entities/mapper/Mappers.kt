package com.example.gestiontienda2.data.local.room.entities.mapper

import com.example.gestiontienda2.data.local.room.entities.*
import com.example.gestiontienda2.data.local.room.entities.entity.ClientEntity
import com.example.gestiontienda2.data.local.room.entities.entity.OrderEntity
import com.example.gestiontienda2.data.local.room.entities.entity.OrderItemEntity
import com.example.gestiontienda2.data.local.room.entities.entity.ProductEntity
import com.example.gestiontienda2.domain.models.*

// ---------- CLIENTES ----------

fun ClientEntity.toDomain(): Client = Client(
    id = this.id.toInt(),
    name = this.name,
    phone = this.phone,
    address = this.address,
    email = this.email,
    paymentPreference = this.paymentPreference // Valor por defecto si es nulo
)

fun Client.toEntity(): ClientEntity = ClientEntity(
    id = this.id.toLong(),
    name = this.name,
    phone = this.phone,
    address = this.address,
    email = this.email ?: "",
    paymentPreference = "efectivo" // Ajusta según lógica si tienes otras opciones
)

// ---------- PRODUCTOS ----------

fun ProductEntity.toDomain(): Product = Product(
    id = this.id.toInt(),
    name = this.name,
    description = this.description,
    price = this.price,
    stockQuantity = this.stockQuantity,
    reservedStockQuantity = this.reservedStockQuantity
)

fun Product.toEntity(): ProductEntity = ProductEntity(
    id = this.id.toLong(),
    name = this.name,
    description = this.description,
    price = this.price,
    stockQuantity = this.stockQuantity,
    reservedStockQuantity = this.reservedStockQuantity
)

// ---------- PEDIDOS (ORDERS) ----------

fun OrderEntity.toDomain(client: Client?, items: List<OrderItem>): Order = Order(
    id = this.id.toInt(),
    clientId = this.clientId.toInt(),
    orderDate = this.orderDate,
    status = this.status,
    totalAmount = this.totalAmount,
    items = items,
    client = client
)

fun Order.toEntity(): OrderEntity = OrderEntity(
    id = this.id.toLong(),
    clientId = this.clientId.toLong(),
    orderDate = this.orderDate,
    status = this.status,
    totalAmount = this.totalAmount
)

// ---------- ITEMS DE PEDIDO ----------

fun OrderItemEntity.toDomain(products: Map<Long, Product>): OrderItem = OrderItem(
    id = this.id.toInt(),
    orderId = this.orderId.toInt(),
    productId = this.productId.toInt(),
    quantity = this.quantity,
    priceAtOrder = this.priceAtOrder,
    product = products[this.productId]
)

fun OrderItem.toEntity(): OrderItemEntity = OrderItemEntity(
    id = this.id.toLong(),
    orderId = this.orderId.toLong(),
    productId = this.productId.toLong(),
    quantity = this.quantity,
    priceAtOrder = this.priceAtOrder
)

// ---------- ORDER WITH ITEMS ----------

fun OrderWithItems.toDomain(clients: List<Client>, products: List<Product>): Order {
    val client = clients.find { it.id == this.order.clientId.toInt() }
    val productsMap = products.associateBy { it.id.toLong() }
    val itemsDomain = this.items.map { it.toDomain(productsMap) }
    return this.order.toDomain(client, itemsDomain)
}
