package ru.innohelpers.innohelp.realm.storage

import android.os.Handler
import io.realm.Realm
import io.realm.kotlin.where
import ru.innohelpers.innohelp.data.delivery.Delivery
import ru.innohelpers.innohelp.realm.models.DeliveryModel

class DeliveryRealmStorage(handler: Handler, realm: Realm) : RealmStorage(handler, realm), IDeliveryStorage {
    override fun storeAllDeliveries(deliveries: Collection<Delivery>) {
        invoke {
            realm.executeTransaction { itRealm ->
                for (delivery in deliveries) {
                    val managedDelivery = toRealmDelivery(delivery)
                    itRealm.copyToRealmOrUpdate(managedDelivery)
                }
            }
        }
    }

    private fun toRealmDelivery(delivery: Delivery): DeliveryModel {
        val managedDelivery = DeliveryModel()
        managedDelivery.id = delivery.id
        managedDelivery.benefit = delivery.benefit
        managedDelivery.closed =  delivery.closed
        managedDelivery.creator = delivery.creator
        managedDelivery.location = delivery.location
        managedDelivery.openTime = delivery.openTime
        managedDelivery.takenBy = delivery.takenBy
        managedDelivery.title = delivery.title
        managedDelivery.totalCost = delivery.totalCost
        managedDelivery.items.addAll(delivery.items)
        return managedDelivery
    }

    private fun toDataDelivery(realmDelivery: DeliveryModel): Delivery {
        return Delivery(realmDelivery.id, realmDelivery.creator, realmDelivery.openTime, realmDelivery.title,
        realmDelivery.location, realmDelivery.totalCost, realmDelivery.benefit, ArrayList(realmDelivery.items), realmDelivery.takenBy, realmDelivery.closed)
    }

    override fun storeDelivery(delivery: Delivery) {
        invoke {
            realm.executeTransaction {
                val managedOrder = toRealmDelivery(delivery)
                it.copyToRealmOrUpdate(managedOrder)
            }
        }
    }

    override fun getAllDeliveries(): ArrayList<Delivery> {
        var foundDeliveries: Collection<Delivery> = emptyList()
        invoke {
            foundDeliveries = realm.where<DeliveryModel>().findAll().map(this::toDataDelivery)
        }
        return ArrayList(foundDeliveries)
    }

    override fun getDeliveryById(deliveryId: String): Delivery? {
        var foundDelivery: Delivery? = null
        invoke {
            foundDelivery = toDataDelivery(realm.where<DeliveryModel>().equalTo("id", deliveryId).findFirst() ?: return@invoke)
        }
        return foundDelivery
    }
}