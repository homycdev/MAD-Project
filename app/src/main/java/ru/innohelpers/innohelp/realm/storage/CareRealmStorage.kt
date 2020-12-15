package ru.innohelpers.innohelp.realm.storage

import android.os.Handler
import io.realm.Realm
import io.realm.kotlin.where
import ru.innohelpers.innohelp.data.care.Care
import ru.innohelpers.innohelp.realm.models.CareModel

class CareRealmStorage(handler: Handler, realm: Realm) : RealmStorage(handler, realm), ICareStorage {

    override fun storeAllCares(cares: Collection<Care>) {
        invoke {
            realm.executeTransaction { itRealm ->
                for (care in cares) {
                    val managedDelivery = toRealmCare(care)
                    itRealm.copyToRealmOrUpdate(managedDelivery)
                }
            }
        }
    }

    private fun toRealmCare(care: Care): CareModel {
        val managedCare = CareModel()
        managedCare.id = care.id
        managedCare.benefit = care.benefit
        managedCare.closed = care.closed
        managedCare.creator = care.creator
        managedCare.description = care.description
        managedCare.openTime = care.openTime
        managedCare.takenBy = care.takenBy
        managedCare.title = care.title
        return managedCare
    }

    private fun toDataCare(realmCare: CareModel): Care {
        return Care(realmCare.id, realmCare.openTime, realmCare.title, realmCare.description, realmCare.creator,
        realmCare.benefit, realmCare.takenBy, realmCare.closed)
    }

    override fun storeCare(care: Care) {
        invoke {
            realm.executeTransaction {
                val managedCare = toRealmCare(care)
                it.copyToRealmOrUpdate(managedCare)
            }
        }
    }

    override fun getAllCares(): ArrayList<Care> {
        var foundCares: Collection<Care> = emptyList()
        invoke {
            foundCares = realm.where<CareModel>().findAll().map(this::toDataCare)
        }
        return ArrayList(foundCares)
    }

    override fun findCareById(careId: String): Care? {
        var foundCare: Care? = null
        invoke {
            foundCare = toDataCare(realm.where<CareModel>().equalTo("id", careId).findFirst() ?: return@invoke)
        }
        return foundCare
    }
}