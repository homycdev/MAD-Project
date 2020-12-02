package ru.innohelpers.innohelp.data.extensions

import ru.innohelpers.innohelp.data.order.Order

fun Collection<Order>.forCreator(userId: String): Collection<Order> {
    val result = ArrayList<Order>()
    for (order in this) if (order.creator == userId) result.add(order)
    return result
}

fun Collection<Order>.takeOpen(): Collection<Order> {
    val result = ArrayList<Order>()
    for (order in this) if (!order.closed) result.add(order)
    return result
}

fun Collection<Order>.findById(orderId: String) : Order? {
    for (order in this) if (order.id == orderId) return order
    return null
}
