package ru.innohelpers.innohelp.services.authentication

import org.apache.commons.codec.digest.DigestUtils
import ru.innohelpers.innohelp.data.user.User
import ru.innohelpers.innohelp.realm.storage.IUserStorage
import ru.innohelpers.innohelp.services.server.IApiServer
import ru.innohelpers.innohelp.view_data.user.UserViewData
import javax.inject.Inject

class UserProvider @Inject constructor(private val server: IApiServer, private val userStorage: IUserStorage) : IUserProvider {


    override var user: User? = null
    override suspend fun register(userName: String, password: String) {
        server.registerUser(userName, calculateHash(password))
        login(userName, calculateHash(password))
    }

    override suspend fun login(userName: String, password: String) {
        val response = server.login(userName, calculateHash(password)) ?: return
        userStorage.storeUser(response)
        user = response
    }

    override suspend fun loginByUserId(userId: String) {
        val response = server.getUser(userId)
        user = response
    }

    override suspend fun getUser(userId: String): User? {
        val found = userStorage.findUserById(userId)
        if (found != null) return found
        val user = server.getUser(userId) ?: return null
        userStorage.storeUser(user)
        return user
    }

    override suspend fun updateUser(userViewData: UserViewData): Boolean {
        if (user == null) return false

        val newData = User(user!!.id, user!!.userName, user!!.profilePhoto, user!!.contactPhone, user!!.rating,
        arrayListOf(), arrayListOf(), arrayListOf())

        if (userViewData.profilePhoto != null)
            newData.profilePhoto = userViewData.profilePhoto
        if (userViewData.contactPhone != null)
            newData.contactPhone = userViewData.contactPhone

        val success = server.editUserData(newData)
        if (success) {
            val newUser = getUser(user!!.id)
            if (newUser != null)
                userStorage.storeUser(newUser)
        }
        return success
    }

    private fun calculateHash(password: String): String {
        return DigestUtils.md5Hex(password)
    }
}