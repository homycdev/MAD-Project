package ru.innohelpers.innohelp.realm.storage

import ru.innohelpers.innohelp.data.order.Order

interface IOrderStorage {

    fun storeAllOrders(orders: Collection<Order>)
    fun storeOrder(order: Order)
    fun getAllOrders(): ArrayList<Order>
    fun findOrderById(orderId: String): Order?

}