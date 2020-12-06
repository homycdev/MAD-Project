package ru.innohelpers.innohelp.data.order

data class OrderItem (
    val id: String,
    val addedBy: String,
    val link: String,
    val title: String,
    val description: String,
    val price: Double = 0.0
)