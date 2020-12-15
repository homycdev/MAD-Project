package ru.innohelpers.innohelp.data.care

import java.util.*

data class Care(
    val id: String,
    val openTime: Date?,
    val title: String,
    val description: String,
    val creator: String,
    val benefit: Double,
    val takenBy: String?,
    val closed: Boolean
)
