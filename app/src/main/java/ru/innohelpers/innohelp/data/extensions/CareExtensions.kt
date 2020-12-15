package ru.innohelpers.innohelp.data.extensions

import ru.innohelpers.innohelp.data.care.Care

fun Collection<Care>.findById(careId: String): Care? {
    for (care in this)
        if (care.id == careId) return care
    return null
}

fun Collection<Care>.takeOpen(): Collection<Care> {
    val cares = ArrayList<Care>()
    for (care in this)
        if (!care.closed) cares.add(care)
    return cares
}

fun Collection<Care>.forOwner(userId: String): Collection<Care> {
    val cares = ArrayList<Care>()
    for (care in this)
        if (care.creator == userId)
            cares.add(care)
    return cares
}