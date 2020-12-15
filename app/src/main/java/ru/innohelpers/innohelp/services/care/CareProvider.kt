package ru.innohelpers.innohelp.services.care

import ru.innohelpers.innohelp.data.care.Care
import ru.innohelpers.innohelp.realm.storage.ICareStorage
import ru.innohelpers.innohelp.services.server.IApiServer

class CareProvider(private val server: IApiServer, private val careStorage: ICareStorage) : ICareProvider {

    override suspend fun createCare(care: Care): String? {
        val response = server.createCare(care) ?: return null
        getCare(response, true)
        return response
    }

    override suspend fun getAll(forceNet: Boolean): ArrayList<Care> {
        val deliveries = careStorage.getAllCares()
        if (forceNet || deliveries.size == 0) {
            val response = ArrayList(server.getAllCares())
            careStorage.storeAllCares(response)
            return response
        }
        return deliveries
    }

    override suspend fun getCare(careId: String, forceNet: Boolean): Care? {
        if (forceNet) {
            val care = server.getCare(careId) ?: return null
            careStorage.storeCare(care)
            return care
        }
        return careStorage.findCareById(careId) ?: getCare(careId, true)
    }

    override suspend fun takeCare(careId: String, userId: String): Boolean {
        val success = server.takeCare(careId, userId)
        if (success) getCare(careId, true)
        return success
    }

    override suspend fun cancelCare(careId: String): Boolean {
        val success = server.cancelCare(careId)
        if (success) getCare(careId, true)
        return success
    }

    override suspend fun closeCare(careId: String): Boolean {
        val success = server.closeCare(careId)
        if (success) getCare(careId, true)
        return success
    }

    override suspend fun completeCare(careId: String): Boolean {
        val success = server.completeCare(careId)
        if (success) getCare(careId, true)
        return success
    }
}