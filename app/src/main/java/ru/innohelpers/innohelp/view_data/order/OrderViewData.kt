package ru.innohelpers.innohelp.view_data.order

import ru.innohelpers.innohelp.view_data.user.UserViewData
import java.util.*
import kotlin.collections.ArrayList

data class OrderViewData(
    var id: String,
    var openTime: Date,
    var title: String,
    var description: String,
    var creator: UserViewData,
    var participants: ArrayList<UserViewData>,
    var items: ArrayList<OrderItemViewData>,
    var totalPrice: Double,
    var closed: Boolean
)
