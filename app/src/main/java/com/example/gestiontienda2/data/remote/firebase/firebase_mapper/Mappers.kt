package com.example.gestiontienda2.data.remote.firebase.firebase_mapper


import com.example.gestiontienda2.data.local.entities.entity.OrderItemEntity
import com.example.gestiontienda2.data.repository.OrderWithItems
import com.example.gestiontienda2.domain.models.Client
import com.example.gestiontienda2.domain.models.Order
import com.example.gestiontienda2.domain.models.OrderItem
import com.example.gestiontienda2.domain.models.Product
import com.google.android.engage.food.datamodel.ProductEntity

// Client mappers (ClientEntity.toDomain and Client.toEntity) removed, now centralized in ClientMapper.kt

// ---------- PRODUCTOS ----------

fun ProductEntity.toDomain(): Product = Product(
    id = this.id.toInt(),
    name = this.name,
    description = this.description,
    price = this.salePrice,
    stockQuantity = this.stockQuantity,
    reservedStockQuantity = this.reservedStockQuantity,
    salePrice = this.salePrice,
    category = this.category,
    purchasePrice = this.purchasePrice,
    stock = this.availableStock = this.stockQuantity - this.reservedStockQuantity,
    barcode = TODO(),
    providerId = TODO(),
    availableStock = TODO(),
    buyingPrice = TODO(),
    sellingPrice = TODO(),
    imageUrl = TODO(),
)

fun Product.toEntity(): ProductEntity = ProductEntity(
    id = this.id.toLong(),
    name = this.name,
    description = this.description,
    salePrice =  this.salePrice,
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
    id = this.id,
    clientId = this.clientId,
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

)

fun OrderItem.toEntity(): OrderItemEntity = OrderItemEntity(
    id = this.id,
    orderId = this.orderId,
    productId = this.productId,
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
