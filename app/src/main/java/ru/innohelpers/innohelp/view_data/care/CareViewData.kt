package ru.innohelpers.innohelp.view_data.care

import ru.innohelpers.innohelp.view_data.user.UserViewData
import java.util.*

data class CareViewData(
    val id: String,
    val openTime: Date?,
    val title: String,
    val description: String,
    val creator: UserViewData,
    val benefit: Double,
    val takenBy: UserViewData?,
    val closed: Boolean
)
