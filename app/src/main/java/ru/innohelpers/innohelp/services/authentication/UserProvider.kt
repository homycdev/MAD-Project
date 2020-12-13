package ru.innohelpers.innohelp.services.authentication

import ru.innohelpers.innohelp.data.user.User
import ru.innohelpers.innohelp.services.server.IApiServer
import javax.inject.Inject

class UserProvider @Inject constructor(private val server: IApiServer) : IUserProvider {


    override var user: User? = null
    override suspend fun register(userName: String, passwordHash: String) {
        server.registerUser(userName, passwordHash)
        login(userName, passwordHash)
    }

    override suspend fun login(userName: String, passwordHash: String) {
        val response = server.login(userName, passwordHash) ?: return
        user = response
    }

    override suspend fun loginByUserId(userId: String) {
        val response = server.getUser(userId)
        user = response
    }

    override suspend fun getUser(userId: String): User? {
        return server.getUser(userId)
    }
}