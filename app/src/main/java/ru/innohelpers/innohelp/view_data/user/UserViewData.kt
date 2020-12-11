package ru.innohelpers.innohelp.view_data.user

data class UserViewData(
    var userName: String,
    var profilePhoto: String?,
    var contactPhone: String?,
    var rating: Double,
    var takenOrders: ArrayList<UserTakeViewData>,
    var takenDeliveries: ArrayList<UserTakeViewData>,
    var takenCares: ArrayList<UserTakeViewData>
)
