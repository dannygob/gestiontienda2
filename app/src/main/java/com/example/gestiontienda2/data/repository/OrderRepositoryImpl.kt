package com.example.gestiontienda2.data.repository

import com.example.gestiontienda2.data.local.room.dao.ClientDao
import com.example.gestiontienda2.data.local.room.dao.OrderDao
import com.example.gestiontienda2.data.local.room.dao.ProductDao
import com.example.gestiontienda2.data.local.room.entities.OrderEntity
import com.example.gestiontienda2.data.local.room.entities.OrderItemEntity
import com.example.gestiontienda2.data.local.room.entities.OrderWithItems
import com.example.gestiontienda2.data.remote.firebase.datasource.OrderFirebaseDataSource
import com.example.gestiontienda2.data.remote.firebase.models.OrderFirebase
import com.example.gestiontienda2.data.remote.firebase.models.OrderItemFirebase
import com.example.gestiontienda2.domain.models.Client
import com.example.gestiontienda2.domain.models.Order
import com.example.gestiontienda2.domain.models.OrderItem
import com.example.gestiontienda2.domain.models.Product
import com.example.gestiontienda2.domain.repository.OrderRepository
import kotlinx.coroutines.flow.*
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

    override suspend fun getOrders(): Flow<List<Order>> = flow {
        try {
            orderFirebaseDataSource.getOrders().collect { firebaseOrders ->
                val roomOrders = firebaseOrders.map { it.toRoomEntity() }
                orderDao.insertOrders(roomOrders)

                val clients = clientDao.getAllClientsBlocking()
                val products = productDao.getAllProductsBlocking()

                emit(firebaseOrders.map { it.toDomain(clients, products) })
            }
        } catch (e: Exception) {
            val roomOrdersWithItems = orderDao.getAllOrdersWithItems()
            val clients = clientDao.getAllClientsBlocking()
            val products = productDao.getAllProductsBlocking()
            emit(roomOrdersWithItems.map { it.toDomain(clients, products) })
        }
    }

    override suspend fun getOrderById(orderId: Int): Order? = withContext(ioDispatcher) {
        try {
            val firebaseOrder = orderFirebaseDataSource.getOrderById(orderId.toString())
            firebaseOrder?.let {
                val roomEntity = it.toRoomEntity()
                orderDao.insertOrder(roomEntity)

                val clients = clientDao.getAllClientsBlocking()
                val products = productDao.getAllProductsBlocking()
                return@withContext it.toDomain(clients, products)
            }
        } catch (e: Exception) {
            val roomOrderWithItems = orderDao.getOrderWithItemsById(orderId)
            return@withContext roomOrderWithItems?.toDomain(
                clientDao.getAllClientsBlocking(),
                productDao.getAllProductsBlocking()
            )
        }
    }

    override suspend fun addOrder(order: Order): Long = withContext(ioDispatcher) {
        val orderId = orderDao.insertOrder(order.toEntity())
        val orderItemEntities = order.items.map { it.toEntity(orderId.toInt()) }
        orderDao.insertOrderItems(orderItemEntities)

        try {
            orderFirebaseDataSource.addOrder(order.toFirebaseModel(orderId.toString()))
        } catch (e: Exception) {
            // Manejo de error en Firebase
        }

        return@withContext orderId
    }

    override suspend fun updateOrder(order: Order) = withContext(ioDispatcher) {
        orderDao.updateOrder(order.toEntity())

        try {
            val firebaseOrder = order.toFirebaseModel(order.id.toString())
            orderFirebaseDataSource.updateOrder(firebaseOrder)
        } catch (e: Exception) {
            // Manejo de error en Firebase
        }
    }

    override suspend fun deleteOrder(order: Order) = withContext(ioDispatcher) {
        try {
            orderFirebaseDataSource.deleteOrder(order.id.toString())
            orderDao.deleteOrder(order.toEntity())
            orderDao.deleteOrderItemsForOrder(order.id)
        } catch (e: Exception) {
            // Manejo de error en Firebase
        }
    }

    override suspend fun updateOrderStatus(orderId: Long, newStatus: String) {
        withContext(ioDispatcher) {
            orderDao.updateOrderStatus(orderId, newStatus)
        }
    }
}

// region Mapeos y funciones auxiliares

private fun OrderFirebase.toRoomEntity(): OrderEntity {
    return OrderEntity(
        id = this.id,
        clientId = this.clientId,
        orderDate = this.orderDate,
        status = this.status,
        totalAmount = this.totalAmount
    )
}

private fun OrderWithItems.toDomain(
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
        product = products[this.productId]
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
        id = this.id.toIntOrNull() ?: -1,
        clientId = this.clientId.toIntOrNull() ?: -1,
        orderDate = this.orderDate,
        status = this.status,
        totalAmount = this.totalAmount,
        items = this.orderItems.map { it.toDomain(products.associateBy { it.id }) }
    )
}

private fun OrderItemFirebase.toDomain(products: Map<Int, Product>): OrderItem {
    return OrderItem(
        id = 0,
        orderId = 0,
        productId = this.productId.toIntOrNull() ?: 0,
        quantity = this.quantity,
        priceAtOrder = this.priceAtOrder,
        product = products[this.productId.toIntOrNull() ?: 0]
    )
}

// endregion
