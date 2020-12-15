package ru.innohelpers.innohelp.realm.models

import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class OrderItemModel : RealmObject() {

    var id: String = ""
    var addedBy: String = ""
    var link: String = ""
    var title: String = ""
    var description: String = ""
    var price: Double = 0.0

}