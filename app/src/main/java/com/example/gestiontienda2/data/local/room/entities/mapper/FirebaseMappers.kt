package com.example.gestiontienda2.data.local.room.entities.mapper

// Import necessary domain and firebase models here as you implement
// e.g.,
import com.example.gestiontienda2.domain.models.*
import com.example.gestiontienda2.data.remote.firebase.models.*
import java.text.SimpleDateFormat
import java.util.Date // Required for Date from Long
import java.util.Locale
// import java.util.TimeZone // if needed for date string conversion

// Date formatter instance
private val firebaseDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

// ---------- OPERATIONAL EXPENSE (ServiceExpenseFirebase <-> OperationalExpense) ----------

fun ServiceExpenseFirebase.toDomain(): OperationalExpense = OperationalExpense(
    id = this.id.toIntOrNull() ?: 0,
    description = this.description,
    date = if (this.date != 0L) firebaseDateFormat.format(Date(this.date)) else "",
    amount = this.amount, // Assuming ServiceExpenseFirebase.amount is Double
    category = this.category,
    notes = this.notes,
    type = this.type ?: "expense" // Assuming 'type' might be missing in Firebase, providing default
)

fun OperationalExpense.toFirebase(): ServiceExpenseFirebase = ServiceExpenseFirebase(
    id = this.id.toString(),
    description = this.description,
    date = try { firebaseDateFormat.parse(this.date)?.time ?: 0L } catch (e: Exception) { 0L },
    amount = this.amount,
    category = this.category,
    notes = this.notes,
    type = this.type
)

// ---------- PRODUCT (ProductFirebase <-> Product) ----------

fun ProductFirebase.toDomain(): Product = Product(
    id = this.id.toIntOrNull() ?: 0,
    name = this.name,
    price = this.price,
    barcode = this.barcode,
    purchasePrice = this.purchasePrice,
    salePrice = this.salePrice,
    category = this.category,
    stock = this.stock, // This seems to be the general stock field from ProductFirebase
    providerId = this.providerId, // Assuming ProductFirebase.providerId is Int
    stockQuantity = this.stockQuantity, // Specific stock quantity
    description = this.description,
    reservedStockQuantity = this.reservedStockQuantity,
    availableStock = this.availableStock
    // Note: Domain Product has 'availableStock', 'reservedStockQuantity'.
    // Firebase ProductFirebase also has these.
    // Domain Product has 'stock', Firebase ProductFirebase has 'stock'.
    // Domain Product has 'providerId', Firebase ProductFirebase has 'providerId'.
)

fun Product.toFirebase(): ProductFirebase = ProductFirebase(
    id = this.id.toString(),
    name = this.name,
    price = this.price,
    barcode = this.barcode,
    purchasePrice = this.purchasePrice,
    salePrice = this.salePrice,
    category = this.category,
    stock = this.stock, // General stock
    providerId = this.providerId,
    stockQuantity = this.stockQuantity, // Specific stock quantity from domain
    description = this.description,
    reservedStockQuantity = this.reservedStockQuantity,
    availableStock = this.availableStock
)

// ---------- PROVIDER (ProviderFirebase <-> Provider) ----------

fun ProviderFirebase.toDomain(): Provider = Provider(
    id = this.id.toIntOrNull() ?: 0,
    name = this.name,
    phone = this.phone ?: "", // Handle potential null from Firebase if domain expects non-null
    address = this.address ?: "", // Handle potential null
    email = this.email
)

fun Provider.toFirebase(): ProviderFirebase = ProviderFirebase(
    id = this.id.toString(),
    name = this.name,
    phone = this.phone,
    address = this.address,
    email = this.email
)

// ---------- ORDER ITEM (OrderItemFirebase <-> OrderItem) ----------

fun OrderItemFirebase.toDomain(): OrderItem = OrderItem(
    // OrderItemFirebase typically doesn't have its own 'id' or 'orderId'
    // These would be set by the calling context if OrderItem domain model requires them
    // and they are not part of OrderItemFirebase structure.
    // For now, assuming OrderItem domain ID can be defaulted or isn't from OrderItemFirebase itself.
    id = 0, // Defaulting, as Firebase nested object usually doesn't have its own ID.
    orderId = 0, // Defaulting, parent Order's ID would be used.
    productId = this.productId.toIntOrNull() ?: 0,
    quantity = this.quantity,
    priceAtOrder = this.priceAtOrder, // Assuming field name matches
    product = null // Product details would be fetched/mapped separately
)

fun OrderItem.toFirebase(): OrderItemFirebase = OrderItemFirebase(
    productId = this.productId.toString(),
    quantity = this.quantity,
    priceAtOrder = this.priceAtOrder // Assuming field name matches
)

// ---------- ORDER (OrderFirebase <-> Order) ----------

fun OrderFirebase.toDomain(): Order = Order(
    id = this.id.toIntOrNull() ?: 0,
    clientId = this.clientId.toIntOrNull() ?: 0, // Assuming clientId in Firebase is String
    orderDate = this.orderDate, // Assuming OrderFirebase.orderDate is Long
    status = this.status,
    totalAmount = this.totalAmount,
    items = this.orderItems.map { it.toDomain() }, // Mapping nested items
    client = null // Client details would be fetched/mapped separately
)

fun Order.toFirebase(): OrderFirebase = OrderFirebase(
    id = this.id.toString(),
    clientId = this.clientId.toString(),
    orderDate = this.orderDate,
    status = this.status,
    totalAmount = this.totalAmount,
    orderItems = this.items.map { it.toFirebase() } // Mapping nested items
)

// ---------- PURCHASE ITEM (PurchaseItemFirebase <-> PurchaseItem) ----------

fun PurchaseItemFirebase.toDomain(): PurchaseItem = PurchaseItem(
    id = 0, // Defaulting, as Firebase nested object usually doesn't have its own ID.
    purchaseId = 0, // Defaulting, parent Purchase's ID would be used.
    productId = this.productId.toIntOrNull() ?: 0,
    quantity = this.quantity,
    purchasePrice = this.purchasePrice, // Assuming field name matches
    product = null // Product details would be fetched/mapped separately
)

fun PurchaseItem.toFirebase(): PurchaseItemFirebase = PurchaseItemFirebase(
    productId = this.productId.toString(),
    quantity = this.quantity,
    purchasePrice = this.purchasePrice // Assuming field name matches
)

// ---------- PURCHASE (PurchaseFirebase <-> Purchase) ----------

fun PurchaseFirebase.toDomain(): Purchase = Purchase(
    id = this.id.toIntOrNull() ?: 0, // Domain 'id' (Int)
    id1 = this.id.toLongOrNull() ?: 0L, // Domain 'id1' (Long) from Firebase String ID
    providerId = this.providerId.toIntOrNull() ?: 0, // Assuming providerId in Firebase is String
    date = this.date, // Assuming PurchaseFirebase.date is Long
    totalAmount = this.totalAmount,
    items = this.purchaseItems.map { it.toDomain() },
    total = this.totalAmount // Domain 'total' can be same as 'totalAmount' from Firebase
)

fun Purchase.toFirebase(): PurchaseFirebase = PurchaseFirebase(
    id = this.id1.toString(), // Using id1 (Long) from domain for Firebase String ID
    providerId = this.providerId.toString(),
    date = this.date,
    totalAmount = this.totalAmount,
    purchaseItems = this.items.map { it.toFirebase() }
)

// ---------- SALE ITEM (SaleItemFirebase <-> SaleItem) ----------

fun SaleItemFirebase.toDomain(): SaleItem = SaleItem(
    id = 0, // Defaulting, as Firebase nested object usually doesn't have its own ID.
    saleId = 0, // Defaulting, parent Sale's ID would be used.
    productId = this.productId.toIntOrNull() ?: 0,
    quantity = this.quantity,
    priceAtSale = this.priceAtSale, // Assuming field name matches
    product = null // Product details would be fetched/mapped separately
)

fun SaleItem.toFirebase(): SaleItemFirebase = SaleItemFirebase(
    productId = this.productId.toString(),
    quantity = this.quantity,
    priceAtSale = this.priceAtSale // Assuming field name matches
)

// ---------- SALE (SaleFirebase <-> Sale) ----------

fun SaleFirebase.toDomain(): Sale = Sale(
    id = this.id.toIntOrNull() ?: 0,
    clientId = this.clientId.toIntOrNull() ?: 0, // Assuming clientId in Firebase is String
    saleDate = this.saleDate, // Assuming SaleFirebase.saleDate is Long
    date = if (this.saleDate != 0L) firebaseDateFormat.format(Date(this.saleDate)) else "", // Formatted date string
    totalAmount = this.totalAmount,
    paymentMethod = this.paymentMethod ?: "",
    total = this.totalAmount, // Domain 'total' can be same as 'totalAmount' from Firebase
    items = this.saleItems.map { it.toDomain() } // Assuming domain Sale.items is List<SaleItem>
)

fun Sale.toFirebase(): SaleFirebase = SaleFirebase(
    id = this.id.toString(),
    clientId = this.clientId.toString(),
    saleDate = this.saleDate, // Domain 'saleDate' (Long)
    totalAmount = this.totalAmount,
    paymentMethod = this.paymentMethod,
    saleItems = this.items.map { it.toFirebase() }
)

// Helper for String to Long for IDs if needed, though direct .toLongOrNull might be fine
fun String.toLongOrNull(): Long? {
    return try {
        this.toLong()
    } catch (e: NumberFormatException) {
        null
    }
}
