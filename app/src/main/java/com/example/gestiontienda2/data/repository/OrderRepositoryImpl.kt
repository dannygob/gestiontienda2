
package com.example.gestiontienda2.data.repository

import com.example.gestiontienda2.data.local.dao.ClientDao
import com.example.gestiontienda2.data.local.dao.OrderDao
import com.example.gestiontienda2.data.local.dao.ProductDao
import com.example.gestiontienda2.data.remote.firebase.datasource.source.OrderFirebaseDataSource
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
import javax.inject.Named
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class OrderRepositoryImpl @Inject constructor(
    private val orderDao: OrderDao,
    private val orderFirebaseDataSource: OrderFirebaseDataSource,
    private val productDao: ProductDao,
    private val clientDao: ClientDao,
    @Named("IODispatcher") private val ioDispatcher: CoroutineContext,
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


// Mapeos auxiliares

