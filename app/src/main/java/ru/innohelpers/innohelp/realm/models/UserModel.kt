package ru.innohelpers.innohelp.realm.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class UserModel : RealmObject() {
    @PrimaryKey var id: String = ""
    @Index var userName: String = ""
    var profilePhoto: String? = ""
    var contactPhone: String? = ""
    var rating: Double = 0.0
    var takenOrders: RealmList<UserTakeInfoModel> = RealmList()
    var takenDeliveries: RealmList<UserTakeInfoModel> = RealmList()
    var takenCares: RealmList<UserTakeInfoModel> = RealmList()
}