package ru.innohelpers.innohelp.data.user

data class User(
    var id: String,
    var userName: String,
    var profilePhoto: String?,
    var contactPhone: String?,
    var rating: Double = 0.0,
    var takenOrders: ArrayList<UserTakeInfo>,
    var takenDeliveries: ArrayList<UserTakeInfo>,
    var takenCares: ArrayList<UserTakeInfo>
)
