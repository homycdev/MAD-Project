package ru.innohelpers.innohelp.services.delivery

import ru.innohelpers.innohelp.data.delivery.Delivery
import ru.innohelpers.innohelp.realm.storage.IDeliveryStorage
import ru.innohelpers.innohelp.services.server.IApiServer
import javax.inject.Inject

class DeliveryProvider @Inject constructor(private val server: IApiServer, private val deliveryStorage: IDeliveryStorage) : IDeliveryProvider {

    override suspend fun createDelivery(delivery: Delivery): String? {
        val response = server.createDelivery(delivery) ?: return null
        getDelivery(response, true)
        return response
    }

    override suspend fun getAll(forceNet: Boolean): ArrayList<Delivery> {
        val deliveries = deliveryStorage.getAllDeliveries()
        if (forceNet || deliveries.size == 0) {
            val response = ArrayList(server.getAllDeliveries())
            deliveryStorage.storeAllDeliveries(response)
            return response
        }
        return deliveries
    }

    override suspend fun getDelivery(deliveryId: String, forceNet: Boolean): Delivery? {
        if (forceNet) {
            val delivery = server.getDelivery(deliveryId) ?: return null
            deliveryStorage.storeDelivery(delivery)
            return delivery
        }
        return deliveryStorage.getDeliveryById(deliveryId) ?: getDelivery(deliveryId, true)
    }

    override suspend fun takeDelivery(deliveryId: String, userId: String): Boolean {
        val success = server.takeDelivery(deliveryId, userId)
        if (success) getDelivery(deliveryId, true)
        return success
    }

    override suspend fun cancelDelivery(deliveryId: String): Boolean {
        val success = server.cancelDelivery(deliveryId)
        if (success) getDelivery(deliveryId, true)
        return success
    }

    override suspend fun closeDelivery(deliveryId: String): Boolean {
        val success = server.closeDelivery(deliveryId)
        if (success) getDelivery(deliveryId, true)
        return success
    }

    override suspend fun completeDelivery(deliveryId: String): Boolean {
        val success = server.completeDelivery(deliveryId)
        if (success) getDelivery(deliveryId, true)
        return success
    }
}