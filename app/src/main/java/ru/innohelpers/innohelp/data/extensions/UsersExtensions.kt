package ru.innohelpers.innohelp.data.extensions

import ru.innohelpers.innohelp.data.user.User

fun Collection<User>.findById(userId: String): User? {
    for (user in this)
        if (user.id == userId) return user
    return null
}

fun Collection<User>.findByUserName(userName: String): User? {
    for (user in this)
        if (user.userName == userName) return user
    return null
}