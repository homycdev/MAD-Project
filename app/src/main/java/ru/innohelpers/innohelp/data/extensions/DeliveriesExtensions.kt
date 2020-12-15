package ru.innohelpers.innohelp.data.extensions

import ru.innohelpers.innohelp.data.delivery.Delivery

fun Collection<Delivery>.findById(deliveryId: String): Delivery? {
    for (delivery in this)
        if (delivery.id == deliveryId) return delivery
    return null
}

fun Collection<Delivery>.takeOpen(): Collection<Delivery> {
    val deliveries = ArrayList<Delivery>()
    for (delivery in this)
        if (!delivery.closed) deliveries.add(delivery)
    return deliveries
}

fun Collection<Delivery>.forOwner(userId: String): Collection<Delivery> {
    val deliveries = ArrayList<Delivery>()
    for (delivery in this)
        if (delivery.creator == userId)
            deliveries.add(delivery)
    return deliveries
}