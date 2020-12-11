package ru.innohelpers.innohelp.view_data.order

import ru.innohelpers.innohelp.view_data.user.UserViewData

data class OrderItemViewData(
    var id: String,
    var title: String,
    var link: String,
    var description: String,
    var addedBy: UserViewData,
    var price: Double
)