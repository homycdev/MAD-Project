package ru.innohelpers.innohelp.services.authentication

import ru.innohelpers.innohelp.data.user.User
import ru.innohelpers.innohelp.view_data.user.UserViewData

interface IUserProvider {
    var user: User?

    suspend fun register(userName: String, password: String)
    suspend fun login(userName: String, password: String)
    suspend fun loginByUserId(userId: String)
    suspend fun getUser(userId: String): User?
    suspend fun updateUser(userViewData: UserViewData): Boolean
}