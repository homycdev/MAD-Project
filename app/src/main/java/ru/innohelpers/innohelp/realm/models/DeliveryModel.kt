package ru.innohelpers.innohelp.realm.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.*


@RealmClass
open class DeliveryModel : RealmObject() {

    @PrimaryKey var id: String = ""
    var creator: String = ""
    var openTime: Date? = null
    var title: String = ""
    var location: String = ""
    var totalCost: Double = 0.0
    var benefit: Double = 0.0
    var items: RealmList<String> = RealmList()
    var takenBy: String? = null
    var closed: Boolean = false

}