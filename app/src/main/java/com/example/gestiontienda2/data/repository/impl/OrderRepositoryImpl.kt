package com.example.gestiontienda2.data.repository.impl

import com.example.gestiontienda2.data.local.dao.ClientDao
import com.example.gestiontienda2.data.local.dao.OrderDao
import com.example.gestiontienda2.data.local.dao.ProductDao
import com.example.gestiontienda2.data.remote.firebase.datasource.source.OrderFirebaseDataSource
import com.example.gestiontienda2.data.remote.firebase.models.OrderFirebase
import com.example.gestiontienda2.data.remote.firebase.models.OrderItemFirebase
import com.example.gestiontienda2.data.repository.OrderWithItems
import com.example.gestiontienda2.domain.models.Client
import com.example.gestiontienda2.domain.models.Order
import com.example.gestiontienda2.domain.models.OrderItem
import com.example.gestiontienda2.domain.models.Product
import com.example.gestiontienda2.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class OrderRepositoryImpl @Inject constructor(
    private val orderDao: OrderDao,
    private val orderFirebaseDataSource: OrderFirebaseDataSource,
    private val productDao: ProductDao,
    private val clientDao: ClientDao,
    private val ioDispatcher: CoroutineContext,
) : OrderRepository {

    override fun getAllOrders(): Flow<List<Order>> = orderDao.getAllOrdersWithItems()
        .map { ordersWithItems ->
            val clients = clientDao.getAllClientsBlocking().map { it.toDomain() }
            val products = productDao.getAllProductsBlocking().map { it.toDomain() }
            ordersWithItems.map { it.toDomain(clients, products) }
        }

    override suspend fun getOrderById(orderId: Int): Order? = withContext(ioDispatcher) {
        try {
            orderFirebaseDataSource.getOrderById(orderId.toString())?.let {
                orderDao.insertOrder(it.toRoomEntity())
                return@withContext it.toDomain(
                    clientDao.getAllClientsBlocking().map { c -> c.toDomain() },
                    productDao.getAllProductsBlocking().map { p -> p.toDomain() }
                )
            }
        } catch (_: Exception) {
            return@withContext orderDao.getOrderWithItemsById(orderId.toLong())
                ?.toDomain(
                    clientDao.getAllClientsBlocking().map { c -> c.toDomain() },
                    productDao.getAllProductsBlocking().map { p -> p.toDomain() }
                )
        }
    }

    override suspend fun addOrder(order: Order): Long = withContext(ioDispatcher) {
        val orderId = orderDao.insertOrder(order.toEntity())
        orderDao.insertOrderItems(order.items.map { it.toEntity(orderId.toInt()) })
        try {
            orderFirebaseDataSource.addOrder(order.toFirebaseModel(orderId.toString()))
        } catch (_: Exception) {
            // Ignorar error Firebase
        }
        orderId
    }

    override suspend fun updateOrder(order: Order) = withContext(ioDispatcher) {
        orderDao.updateOrder(order.toEntity())
        try {
            orderFirebaseDataSource.updateOrder(order.toFirebaseModel(order.id.toString()))
        } catch (_: Exception) {
            // Ignorar error Firebase
        }
    }

    override suspend fun deleteOrder(order: Order) = withContext(ioDispatcher) {
        orderDao.deleteOrder(order.toEntity())
        orderDao.deleteOrderItemsForOrder(order.id.toLong())
        try {
            orderFirebaseDataSource.deleteOrder(order.id.toString())
        } catch (_: Exception) {
            // Ignorar error Firebase
        }
    }

    override suspend fun updateOrderStatus(orderId: Long, newStatus: String) {
        withContext(ioDispatcher) {
            orderDao.updateOrderStatus(orderId, newStatus)
        }
    }
}

private fun OrderFirebase.toRoomEntity(): OrderEntity = OrderEntity(
    id = this.id.toLongOrNull() ?: 0L,
    clientId = this.clientId.toLongOrNull() ?: 0L,
    orderDate = this.orderDate,
    status = this.status,
    totalAmount = this.totalAmount
)

private fun OrderWithItems.toDomain(
    clients: List<Client>,
    products: List<Product>
): Order {
    clients.find { it.id.toLong() == this.order.clientId }
    val productsMap = products.associateBy { it.id }
    return Order(
        id = this.order.id.toInt(),
        clientId = this.order.clientId.toInt(),
        orderDate = this.order.orderDate,
        status = this.order.status,
        totalAmount = this.order.totalAmount,
        items = this.items.map { it.toDomain(productsMap) }
    )
}

private fun OrderItemEntity.toDomain(products: Map<Int, Product>): OrderItem = OrderItem(
    id = this.id,
    orderId = this.orderId,
    productId = this.productId,
    quantity = this.quantity,
    priceAtOrder = this.priceAtOrder,
    product = products[this.productId]
)

private fun Order.toEntity(): OrderEntity = OrderEntity(
    id = this.id.toLong(),
    clientId = this.clientId.toLong(),
    orderDate = this.orderDate,
    status = this.status,
    totalAmount = this.totalAmount
)

private fun OrderItem.toEntity(orderId: Int): OrderItemEntity = OrderItemEntity(
    id = this.id,
    orderId = orderId.toLong(),
    productId = this.productId,
    quantity = this.quantity,
    priceAtOrder = this.priceAtOrder
)

private fun Order.toFirebaseModel(id: String): OrderFirebase = OrderFirebase(
    id = id,
    clientId = this.clientId.toString(),
    orderDate = this.orderDate,
    status = this.status,
    totalAmount = this.totalAmount,
    orderItems = this.items.map { it.toFirebaseModel() }
)

private fun OrderItem.toFirebaseModel(): OrderItemFirebase = OrderItemFirebase(
    productId = this.productId.toString(),
    quantity = this.quantity,
    priceAtOrder = this.priceAtOrder
)

private fun OrderFirebase.toDomain(clients: List<Client>, products: List<Product>): Order {
    clients.find { it.id.toString() == this.clientId }
    val productsMap = products.associateBy { it.id }
    return Order(
        id = this.id.toIntOrNull() ?: -1,
        clientId = this.clientId.toIntOrNull() ?: -1,
        orderDate = this.orderDate,
        status = this.status,
        totalAmount = this.totalAmount,
        items = this.orderItems.map { it.toDomain(productsMap) }
    )
}

private fun OrderItemFirebase.toDomain(products: Map<Int, Product>): OrderItem = OrderItem(
    id = 0,
    orderId = 0,
    productId = this.productId.toIntOrNull() ?: 0,
    quantity = this.quantity,
    priceAtOrder = this.priceAtOrder,
    product = products[this.productId.toIntOrNull() ?: 0]
)