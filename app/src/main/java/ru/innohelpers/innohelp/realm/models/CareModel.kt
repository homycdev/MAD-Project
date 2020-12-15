package ru.innohelpers.innohelp.realm.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.*

@RealmClass
open class CareModel : RealmObject() {

    @PrimaryKey var id: String = ""
    var openTime: Date? = null
    var title: String = ""
    var description: String = ""
    var creator: String = ""
    var benefit: Double = 0.0
    var takenBy: String? = ""
    var closed: Boolean = false

}