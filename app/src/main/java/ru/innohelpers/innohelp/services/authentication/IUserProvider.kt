package ru.innohelpers.innohelp.services.authentication

import ru.innohelpers.innohelp.data.user.User

interface IUserProvider {
    var user: User?

    suspend fun register(userName: String, passwordHash: String)
    suspend fun login(userName: String, passwordHash: String)
    suspend fun loginByUserId(userId: String)
    suspend fun getUser(userId: String): User?
}