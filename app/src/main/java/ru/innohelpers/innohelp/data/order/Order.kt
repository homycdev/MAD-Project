package ru.innohelpers.innohelp.data.order

import java.util.*
import kotlin.collections.ArrayList

data class Order (
    var id: String,
    var openTime: Date?,
    var title: String?,
    var description: String?,
    var creator: String,
    var participants: ArrayList<String>,
    var items: ArrayList<OrderItem>,
    var totalPrice: Double = 0.0,
    var closed: Boolean = false
)