package ru.innohelpers.innohelp.realm.storage

import ru.innohelpers.innohelp.data.care.Care

interface ICareStorage {

    fun storeAllCares(cares: Collection<Care>)
    fun storeCare(care: Care)
    fun getAllCares(): ArrayList<Care>
    fun findCareById(careId: String): Care?

}