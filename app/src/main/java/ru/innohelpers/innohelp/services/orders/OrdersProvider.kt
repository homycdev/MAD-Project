package ru.innohelpers.innohelp.services.orders

import ru.innohelpers.innohelp.data.order.Order
import ru.innohelpers.innohelp.data.order.OrderItem
import ru.innohelpers.innohelp.realm.storage.IOrderStorage
import ru.innohelpers.innohelp.services.server.IApiServer
import javax.inject.Inject

class OrdersProvider @Inject constructor(private val server: IApiServer, private val orderStorage: IOrderStorage) : IOrdersProvider {

    override suspend fun getAllOrders(forceNet: Boolean): ArrayList<Order> {
        val orders = orderStorage.getAllOrders()
        if (forceNet || orders.size == 0) {
            val ordersResponse = server.getAllOrders()
            orderStorage.storeAllOrders(ordersResponse)
            return ArrayList(ordersResponse)
        }
        return orders
    }

    override suspend fun getOrder(orderId: String, forceNet: Boolean): Order {
        if (forceNet) {
            val order =  server.getOrder(orderId)
            orderStorage.storeOrder(order)
            return order
        }
        return orderStorage.findOrderById(orderId) ?: return getOrder(orderId, true)
    }

    override suspend fun createOrder(order: Order): String? {
        val orderId = server.createNewOrder(order)
        if (orderId != null)
            getOrder(orderId, true)
        return orderId
    }

    override suspend fun joinOrder(orderId: String, userId: String): Boolean {
        val success =  server.addParticipantToOrder(orderId, userId)
        if (success) getOrder(orderId, true)
        return success
    }

    override suspend fun addItem(orderId: String, item: OrderItem): String? {
        val itemId = server.addItemToOrder(orderId, item)
        if (itemId != null) getOrder(orderId, true)
        return itemId
    }

    override suspend fun removeItem(orderId: String, itemId: String): Boolean {
        val success = server.removeItemFromOrder(orderId, itemId)
        if (success) getOrder(orderId, true)
        return success
    }

    override suspend fun leaveOrder(orderId: String, userId: String): Boolean {
        val success = server.removeParticipantToOrder(orderId, userId)
        if (success) getOrder(orderId, true)
        return success
    }

    override suspend fun closeOrder(orderId: String): Boolean {
        val success = server.closeOrder(orderId)
        if (success) getOrder(orderId, true)
        return success
    }

}