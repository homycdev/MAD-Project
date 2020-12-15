package ru.innohelpers.innohelp.realm.storage

import ru.innohelpers.innohelp.data.user.User

interface IUserStorage {

    fun storeUser(user: User)
    fun findUserById(userId: String): User?
    fun findUserByUserName(userName: String): User?

}