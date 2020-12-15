package ru.innohelpers.innohelp.realm.models

import io.realm.RealmObject
import io.realm.annotations.RealmClass
import java.util.*

@RealmClass
open class UserTakeInfoModel : RealmObject() {
    var id: String = ""
    var status: Int = -1
    var takeTime: Date = Date()
    var completionTime: Date = Date()
}