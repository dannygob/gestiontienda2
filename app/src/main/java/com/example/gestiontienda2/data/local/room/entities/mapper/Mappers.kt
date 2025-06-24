package com.example.gestiontienda2.data.local.room.entities.mapper

import com.example.gestiontienda2.data.local.room.entities.*
import com.example.gestiontienda2.data.local.room.entities.entity.ClientEntity
import com.example.gestiontienda2.data.local.room.entities.entity.OrderEntity
import com.example.gestiontienda2.data.local.room.entities.entity.OrderItemEntity
import com.example.gestiontienda2.data.local.room.entities.entity.ProductEntity
import com.example.gestiontienda2.domain.models.*

// Client mappers (ClientEntity.toDomain and Client.toEntity) removed, now centralized in ClientMapper.kt

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
    product = products[this.productId]
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

// ---------- PROVIDERS ----------

fun ProviderEntity.toDomain(): Provider = Provider(
    id = this.id,
    name = this.name,
    phone = this.phone ?: "",
    address = this.address ?: "",
    email = this.email // Domain email is nullable
)

fun Provider.toEntity(): ProviderEntity = ProviderEntity(
    id = this.id,
    name = this.name,
    phone = this.phone,
    address = this.address,
    email = this.email
)

// ---------- PURCHASES ----------

fun PurchaseEntity.toDomain(items: List<PurchaseItem>): Purchase = Purchase(
    id = this.id.toInt(), // Assuming domain 'id' is Int, entity 'id' is Long
    id1 = this.id,        // Mapping entity 'id' (Long) to domain 'id1' (Long)
    providerId = this.providerId.toInt(),
    date = this.date, // Entity 'date' (Long) to domain 'date' (Long)
    total = this.total,
    totalAmount = this.totalAmount ?: this.total, // Use totalAmount if available, else fallback to total
    items = items
)

fun Purchase.toEntity(): PurchaseEntity = PurchaseEntity(
    id = this.id1, // Use id1 from domain which should hold the original Long id
    providerId = this.providerId.toLong(),
    date = this.date,
    total = this.total,
    // purchaseDate and totalAmount are other fields in PurchaseEntity,
    // if they need to be mapped from domain, add them to Purchase domain model
    // For now, setting totalAmount from domain's totalAmount or total
    totalAmount = this.totalAmount,
    purchaseDate = this.date // Assuming purchaseDate is same as date from domain for now
)

// ---------- PURCHASE ITEMS ----------

fun PurchaseItemEntity.toDomain(productMap: Map<Long, Product>? = null): PurchaseItem = PurchaseItem(
    id = this.id.toInt(),
    purchaseId = this.purchaseId.toInt(),
    productId = this.productId.toInt(),
    quantity = this.quantity,
    purchasePrice = this.unitPrice,
    product = productMap?.get(this.productId) // Product is populated externally if map is provided
)

fun PurchaseItem.toEntity(): PurchaseItemEntity = PurchaseItemEntity(
    id = this.id.toLong(),
    purchaseId = this.purchaseId.toLong(),
    productId = this.productId.toLong(),
    quantity = this.quantity,
    unitPrice = this.purchasePrice
)

// ---------- SALES ----------

fun SaleEntity.toDomain(items: List<SaleItem>): Sale = Sale(
    id = this.id.toInt(),
    clientId = this.clientId.toInt(),
    saleDate = this.date, // Entity 'date' (Long) maps to domain 'saleDate' (Long)
    date = "", // Domain 'date' (String) is likely a formatted version, set to empty or handle elsewhere
    total = this.total,
    totalAmount = this.total, // Assuming totalAmount is same as total from entity
    paymentMethod = "", // Not available in SaleEntity, default to empty
    items = items.map { it } // Ensuring correct type if Sale.items expects List<Product.SaleItem>
                               // If domain SaleItem is different from Product.SaleItem, this mapping needs adjustment
                               // For now, assuming it's List<com.example.gestiontienda2.domain.models.SaleItem>
)

fun Sale.toEntity(): SaleEntity = SaleEntity(
    id = this.id.toLong(),
    clientId = this.clientId.toLong(),
    date = this.saleDate, // Domain 'saleDate' (Long) maps to entity 'date' (Long)
    total = this.total
)

// ---------- SALE ITEMS ----------

fun SaleItemEntity.toDomain(productMap: Map<Long, Product>? = null): SaleItem = SaleItem(
    id = this.id.toInt(),
    saleId = this.saleId.toInt(),
    productId = this.productId.toInt(),
    quantity = this.quantity,
    priceAtSale = this.unitPrice,
    product = productMap?.get(this.productId) // Product is populated externally if map is provided
)

fun SaleItem.toEntity(): SaleItemEntity = SaleItemEntity(
    id = this.id.toLong(),
    saleId = this.saleId.toLong(),
    productId = this.productId.toLong(),
    quantity = this.quantity,
    unitPrice = this.priceAtSale
)

// ---------- OPERATIONAL EXPENSES (formerly Service Expenses) ----------

fun ServiceExpenseEntity.toDomain(): OperationalExpense = OperationalExpense(
    id = this.id,
    description = this.description,
    date = this.date,
    amount = this.amount.toDoubleOrNull() ?: 0.0, // Convert String from Entity to Double for Domain
    category = this.category,
    notes = this.notes,
    type = this.type
)

fun OperationalExpense.toEntity(): ServiceExpenseEntity = ServiceExpenseEntity(
    id = this.id,
    description = this.description,
    date = this.date,
    amount = this.amount.toString(), // Convert Double from Domain to String for Entity
    category = this.category,
    notes = this.notes,
    type = this.type
)
