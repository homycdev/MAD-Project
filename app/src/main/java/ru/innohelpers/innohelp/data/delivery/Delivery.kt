package ru.innohelpers.innohelp.data.delivery

import java.util.*

data class Delivery(
    val id: String,
    val creator: String,
    val openTime: Date?,
    val title: String,
    val location: String,
    val totalCost: Double,
    val benefit: Double,
    val items: List<String>,
    val takenBy: String?,
    val closed: Boolean
)
