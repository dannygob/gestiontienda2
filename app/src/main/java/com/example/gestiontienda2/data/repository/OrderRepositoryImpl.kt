package com.gestiontienda2.data.repository

import com.gestiontienda2.data.local.room.dao.ClientDao
import com.gestiontienda2.data.local.room.dao.OrderDao
import com.gestiontienda2.data.local.room.dao.ProductDao
import com.gestiontienda2.data.local.room.entities.OrderEntity
import com.gestiontienda2.data.local.room.entities.OrderItemEntity
import com.gestiontienda2.data.remote.firebase.datasource.OrderFirebaseDataSource
import com.gestiontienda2.data.remote.firebase.models.OrderFirebase
import com.gestiontienda2.data.remote.firebase.models.OrderItemFirebase
import com.example.gestiontienda2.domain.models.Client
import com.example.gestiontienda2.domain.models.Order
import com.example.gestiontienda2.domain.models.OrderItem
import com.gestiontienda2.domain.models.Product
import com.gestiontienda2.domain.repository.OrderRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class OrderRepositoryImpl @Inject constructor(
    private val orderDao: OrderDao,
    private val orderFirebaseDataSource: OrderFirebaseDataSource,
    private val productDao: ProductDao, // Inject ProductDao
    private val clientDao: ClientDao,    // Inject ClientDao
    private val ioDispatcher: CoroutineContext // Inject dispatcher
) : OrderRepository {

    override fun getOrders(): Flow<List<Order>> =
        // Combine flows from Room, ClientDao, and ProductDao
        orderDao.getAllOrdersWithItems()
            .combine(clientDao.getAllClients()) { ordersWithItems, clients ->
                Pair(ordersWithItems, clients)
            }
            .combine(productDao.getAllProducts()) { (ordersWithItems, clients), products ->
                // Map Room entities to domain models using fetched clients and products
                ordersWithItems.map { it.toDomain(clients, products) }
            }
            .onEach {
                // Try to fetch from Firebase in the background and update Room
                // This ensures the UI gets data quickly from Room while syncing with Firebase
                // TODO: Implement a more robust synchronization strategy
                // For now, a basic fetch and insert if online
            }
    try
    {
        orderFirebaseDataSource.getOrders().collect { firebaseOrders ->
            val roomOrders = firebaseOrders.map { it.toRoomEntity() }
            orderDao.insertOrders(roomOrders) // Insert into Room
            // Fetch clients and products for mapping to domain
            val clients =
                clientDao.getAllClientsBlocking() // Blocking call for simplicity, consider Flow
            val products =
                productDao.getAllProductsBlocking() // Blocking call for simplicity, consider Flow
            emit(firebaseOrders.map {
                it.toDomain(
                    clients,
                    products
                )
            }) // Emit Firebase data mapped to domain
        }
    } catch (e: Exception)
    {
        // If offline or Firebase error, rely on Room
        orderDao.getAllOrdersWithItems().map { roomOrdersWithItems ->
            // Fetch clients and products for mapping to domain
            val clients = clientDao.getAllClientsBlocking()
            val products = productDao.getAllProductsBlocking()
            roomOrdersWithItems.map { it.toDomain(clients, products) }
        }.collect { emit(it) }
    }
}

override suspend fun updateOrderStatus(orderId: Long, newStatus: String) =
    withContext(ioDispatcher) {
        orderDao.updateOrderStatus(orderId, newStatus) // Update status in Room
        // TODO: Consider updating status in Firebase as well if needed for synchronization
    }


override suspend fun getOrderById(orderId: Int): Order? = withContext(ioDispatcher) {
    // Try Firebase first
    try {
        val firebaseOrder = orderFirebaseDataSource.getOrderById(orderId.toString())
        if (firebaseOrder != null) {
            val roomEntity = firebaseOrder.toRoomEntity()
            orderDao.insertOrder(roomEntity) // Cache in Room
            // Fetch clients and products for mapping to domain
            val clients = clientDao.getAllClientsBlocking()
            val products = productDao.getAllProductsBlocking()
            return@withContext firebaseOrder.toDomain(clients, products)
        }
    } catch (e: Exception) {
        // If Firebase fails, get from Room
        val roomOrderWithItems = orderDao.getOrderWithItemsById(orderId)
        if (roomOrderWithItems != null) {
            // Fetch clients and products for mapping to domain
            val clients = clientDao.getAllClientsBlocking()
            val products = productDao.getAllProductsBlocking()
            return@withContext roomOrderWithItems.toDomain(clients, products)
        }
    }
    // If not found in Firebase, try Room
    val roomOrderWithItems = orderDao.getOrderWithItemsById(orderId)
    if (roomOrderWithItems != null) {
        val clients = clientDao.getAllClientsBlocking()
        val products = productDao.getAllProductsBlocking()
        return@withContext roomOrderWithItems.toDomain(clients, products)
    }
    return@withContext null
}

override suspend fun addOrder(order: Order): Long = withContext(ioDispatcher) {
    val orderId = orderDao.insertOrder(order.toEntity()) // Insert into Room first
    val orderItemEntities = order.items.map { it.toEntity(orderId.toInt()) }
    orderDao.insertOrderItems(orderItemEntities)

    // Add to Firebase
    try {
        val firebaseOrder = order.toFirebaseModel(orderId.toString())
        orderFirebaseDataSource.addOrder(firebaseOrder)
    } catch (e: Exception) {
        // Handle Firebase add error (e.g., log, show message)
    }

    return@withContext orderId
}

override suspend fun updateOrder(order: Order) = withContext(ioDispatcher) {
    orderDao.updateOrder(order.toEntity()) // Update Room
    // TODO: Update order items in Room (delete old and insert new)

    // Update Firebase
    try {
        val firebaseOrder = order.toFirebaseModel(order.id.toString())
        orderFirebaseDataSource.updateOrder(firebaseOrder)
    } catch (e: Exception) {
        // Handle Firebase update error
    }
}

override suspend fun deleteOrder(order: Order) = withContext(ioDispatcher) {
    orderDao.deleteOrder(order.toEntity()) // Delete from Room
    orderDao.deleteOrderItemsForOrder(order.id) // Delete associated items

    // Delete from Firebase
    try {
        orderFirebaseDataSource.deleteOrder(order.id.toString())
    } catch (e: Exception) {
        // Handle Firebase delete error
    }
}

// region Mappers

private fun OrderFirebase.toRoomEntity(): OrderEntity {
    return OrderEntity(
        id = this.id,
        clientId = this.clientId,
        orderDate = this.orderDate,
        status = this.status,
        totalAmount = this.totalAmount
    )
}

private fun com.gestiontienda2.data.local.room.entities.OrderWithItems.toDomain(
    clients: List<Client>,
    products: List<Product>
): Order {
    return OrderEntity(
        id = this.order.id,
        clientId = this.order.clientId,
        orderDate = this.order.orderDate,
        status = this.order.status,
        totalAmount = this.order.totalAmount
    ).toDomain(
        clients.find { it.id == this.order.clientId },
        this.items.map { it.toDomain(products.associateBy { it.id }) })
}

private fun OrderItemEntity.toDomain(products: Map<Int, Product>): OrderItem {
    return OrderItem(
        id = this.id,
        orderId = this.orderId,
        productId = this.productId,
        quantity = this.quantity,
        priceAtOrder = this.priceAtOrder,
        product = products[this.productId] // Get product details
    )
}

private fun Order.toEntity(): OrderEntity {
    return OrderEntity(
        id = this.id,
        clientId = this.clientId,
        orderDate = this.orderDate,
        status = this.status,
        totalAmount = this.totalAmount
    )
}

private fun OrderItem.toEntity(orderId: Int): OrderItemEntity {
    return OrderItemEntity(
        id = this.id,
        orderId = orderId,
        productId = this.productId,
        quantity = this.quantity,
        priceAtOrder = this.priceAtOrder
    )
}

private fun Order.toFirebaseModel(id: String): OrderFirebase {
    return OrderFirebase(
        id = id,
        clientId = this.clientId.toString(),
        orderDate = this.orderDate,
        status = this.status,
        totalAmount = this.totalAmount,
        orderItems = this.items.map { it.toFirebaseModel() }
    )
}

private fun OrderItem.toFirebaseModel(): OrderItemFirebase {
    return OrderItemFirebase(
        productId = this.productId.toString(),
        quantity = this.quantity,
        priceAtOrder = this.priceAtOrder
    )
}

private fun OrderFirebase.toDomain(clients: List<Client>, products: List<Product>): Order {
    val client = clients.find { it.id.toString() == this.clientId }
    return Order(
        id = this.id.toIntOrNull() ?: 0, // Handle potential ID conversion issues
        clientId = this.clientId.toIntOrNull() ?: 0,
        orderDate = this.orderDate,
        status = this.status,
        totalAmount = this.totalAmount,
        items = this.orderItems.map { it.toDomain(products.associateBy { it.id }) }
    )
}

private fun OrderItemFirebase.toDomain(products: Map<Int, Product>): OrderItem {
    return OrderItem(
        id = 0, // Firebase items might not have a separate ID like Room
        orderId = 0, // Order ID will be set when mapping the whole order
        productId = this.productId.toIntOrNull() ?: 0,
        quantity = this.quantity,
        priceAtOrder = this.priceAtOrder,
        product = products[this.productId.toIntOrNull() ?: 0] // Get product details
    )
}

// Helper function for RoomEntity to Domain mapping (used by OrderWithItems)
private fun OrderEntity.toDomain(client: Client?, items: List<OrderItem>): Order {
    return Order(
        id = this.id,
        clientId = this.clientId,
        orderDate = this.orderDate,
        status = this.status,
        totalAmount = this.totalAmount,
        items = items
    )
}

// endregion
}