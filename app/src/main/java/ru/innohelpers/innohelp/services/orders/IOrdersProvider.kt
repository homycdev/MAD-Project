package ru.innohelpers.innohelp.services.orders

import ru.innohelpers.innohelp.data.order.Order
import ru.innohelpers.innohelp.data.order.OrderItem

interface IOrdersProvider {

    suspend fun getAllOrders(forceNet: Boolean) : ArrayList<Order>
    suspend fun getOrder(orderId: String, forceNet: Boolean) : Order
    suspend fun createOrder(order: Order): String?
    suspend fun joinOrder(orderId: String, userId: String): Boolean
    suspend fun addItem(orderId: String, item: OrderItem): String?
    suspend fun removeItem(orderId: String, itemId: String): Boolean
    suspend fun leaveOrder(orderId: String, userId: String): Boolean
    suspend fun closeOrder(orderId: String): Boolean
}