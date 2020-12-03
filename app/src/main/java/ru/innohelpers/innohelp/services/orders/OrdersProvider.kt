package ru.innohelpers.innohelp.services.orders

import ru.innohelpers.innohelp.data.extensions.findById
import ru.innohelpers.innohelp.data.order.Order
import ru.innohelpers.innohelp.data.order.OrderItem
import ru.innohelpers.innohelp.services.server.IApiServer
import javax.inject.Inject

class OrdersProvider @Inject constructor(private val server: IApiServer) : IOrdersProvider {

    private var orders: ArrayList<Order> = ArrayList()

    override suspend fun getAllOrders(forceNet: Boolean): ArrayList<Order> {
        if (forceNet || orders.size == 0) {
            orders = ArrayList(server.getAllOrders())
        }
        return orders
    }

    override suspend fun getOrder(orderId: String, forceNet: Boolean): Order {
        if (forceNet) {
            val order =  server.getOrder(orderId)
            val foundOrder = orders.findById(orderId)
            if (foundOrder != null)
                orders.remove(foundOrder)
            orders.add(order)
            return order
        }
        return orders.findById(orderId) ?: return getOrder(orderId, true)
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