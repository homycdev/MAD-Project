package ru.innohelpers.innohelp.services.care

import ru.innohelpers.innohelp.data.care.Care

interface ICareProvider {

    suspend fun createCare(care: Care): String?
    suspend fun getAll(forceNet: Boolean): ArrayList<Care>
    suspend fun getCare(careId: String, forceNet: Boolean): Care?
    suspend fun takeCare(careId: String, userId: String): Boolean
    suspend fun cancelCare(careId: String): Boolean
    suspend fun closeCare(careId: String): Boolean
    suspend fun completeCare(careId: String): Boolean

}