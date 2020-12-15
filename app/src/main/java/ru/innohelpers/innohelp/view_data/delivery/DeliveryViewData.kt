package ru.innohelpers.innohelp.view_data.delivery

import ru.innohelpers.innohelp.view_data.user.UserViewData
import java.util.*
import kotlin.collections.ArrayList

data class DeliveryViewData(
    val id: String,
    val openTime: Date,
    val title: String,
    val location: String,
    val totalCost: Double,
    val benefit: Double,
    val creator: UserViewData,
    val takenBy: UserViewData?,
    val items: ArrayList<String>,
    val closed: Boolean
)
