package ru.innohelpers.innohelp.data.user

import java.util.*

data class UserTakeInfo(
    var id: String,
    var status: Int = -1,
    var takeTime: Date,
    var completionTime: Date
)
