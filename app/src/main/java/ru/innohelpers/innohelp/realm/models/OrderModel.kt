package ru.innohelpers.innohelp.realm.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.*

@RealmClass
open class OrderModel : RealmObject() {
    @PrimaryKey var id: String = ""
    var openTime: Date? = null
    var title: String? = ""
    var description: String? = ""
    var creator: String = ""
    var participants: RealmList<String> = RealmList()
    var items: RealmList<OrderItemModel> = RealmList()
    var totalPrice: Double = 0.0
    var closed: Boolean = false

}