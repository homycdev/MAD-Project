package ru.innohelpers.innohelp.services.delivery

import ru.innohelpers.innohelp.data.delivery.Delivery

interface IDeliveryProvider {
    suspend fun createDelivery(delivery: Delivery): String?
    suspend fun getAll(forceNet: Boolean): ArrayList<Delivery>
    suspend fun getDelivery(deliveryId: String, forceNet: Boolean): Delivery?
    suspend fun takeDelivery(deliveryId: String, userId: String): Boolean
    suspend fun cancelDelivery(deliveryId: String): Boolean
    suspend fun closeDelivery(deliveryId: String): Boolean
    suspend fun completeDelivery(deliveryId: String): Boolean
}