package ru.innohelpers.innohelp.realm.storage

import android.os.Handler
import io.realm.Realm
import io.realm.kotlin.where
import ru.innohelpers.innohelp.data.order.Order
import ru.innohelpers.innohelp.data.order.OrderItem
import ru.innohelpers.innohelp.realm.models.OrderItemModel
import ru.innohelpers.innohelp.realm.models.OrderModel

class OrderRealmStorage(handler: Handler, realm: Realm) : RealmStorage(handler, realm), IOrderStorage {

    override fun storeAllOrders(orders: Collection<Order>) {
        invoke {
            realm.executeTransaction { itRealm ->
                for (order in orders) {
                    val managedOrder = toRealmOrder(order)
                    itRealm.copyToRealmOrUpdate(managedOrder)
                }
            }
        }
    }

    private fun toRealmOrder(order: Order): OrderModel {
        val managedOrder = OrderModel()
        managedOrder.id = order.id
        managedOrder.closed = order.closed
        managedOrder.creator = order.creator
        managedOrder.description = order.description
        managedOrder.openTime = order.openTime
        managedOrder.title = order.title
        managedOrder.totalPrice = order.totalPrice
        managedOrder.participants.addAll(order.participants)
        managedOrder.items.addAll(order.items.map {
            val managedItem = OrderItemModel()
            managedItem.addedBy = it.addedBy
            managedItem.description = it.description
            managedItem.id = it.id
            managedItem.link = it.link
            managedItem.price = it.price
            managedItem.title = it.title
            return@map managedItem
        })
        return managedOrder
    }

    override fun storeOrder(order: Order) {
        invoke {
            realm.executeTransaction {
                val managedOrder = toRealmOrder(order)
                it.copyToRealmOrUpdate(managedOrder)
            }
        }
    }

    override fun getAllOrders(): ArrayList<Order> {
        var foundOrders: Collection<Order> = emptyList()
        invoke {
            foundOrders = realm.where<OrderModel>().findAll().map(this::toDataOrder)
        }
        return ArrayList(foundOrders)
    }

    private fun toDataOrder(orderModel: OrderModel): Order {
        return Order(
            orderModel.id, orderModel.openTime, orderModel.title, orderModel.description, orderModel.creator,
            ArrayList(orderModel.participants), ArrayList(orderModel.items.map {
                OrderItem(it.id, it.addedBy, it.link, it.title, it.description, it.price)
            }), orderModel.totalPrice, orderModel.closed
        )
    }

    override fun findOrderById(orderId: String): Order? {
        var foundOrder: Order? = null
        invoke {
            foundOrder = toDataOrder(realm.where<OrderModel>().equalTo("id", orderId).findFirst() ?: return@invoke)
        }
        return foundOrder
    }

}