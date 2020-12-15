package ru.innohelpers.innohelp.realm.storage

import ru.innohelpers.innohelp.data.delivery.Delivery

interface IDeliveryStorage {

    fun storeAllDeliveries(deliveries: Collection<Delivery>)
    fun storeDelivery(delivery: Delivery)
    fun getAllDeliveries(): ArrayList<Delivery>
    fun getDeliveryById(deliveryId: String): Delivery?

}