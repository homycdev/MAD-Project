package ru.innohelpers.innohelp.realm.storage

import android.os.Handler
import io.realm.Realm
import io.realm.kotlin.where
import ru.innohelpers.innohelp.data.user.User
import ru.innohelpers.innohelp.data.user.UserTakeInfo
import ru.innohelpers.innohelp.realm.models.UserModel
import ru.innohelpers.innohelp.realm.models.UserTakeInfoModel


class UserRealmStorage(handler: Handler, realm: Realm) : RealmStorage(handler, realm), IUserStorage {

    override fun storeUser(user: User) {
        invoke {
            realm.executeTransaction { itRealm ->
                val managedUser = UserModel()
                managedUser.id = user.id
                managedUser.contactPhone = user.contactPhone
                managedUser.profilePhoto = user.profilePhoto
                managedUser.rating = user.rating
                managedUser.userName = user.userName
                managedUser.takenOrders.addAll(user.takenOrders.map(this::takeInfoToModelMapping))
                managedUser.takenDeliveries.addAll(user.takenDeliveries.map(this::takeInfoToModelMapping))
                managedUser.takenCares.addAll(user.takenCares.map(this::takeInfoToModelMapping))
                itRealm.copyToRealmOrUpdate(managedUser)
            }
        }
    }

    private fun takeInfoToModelMapping(takeInfo: UserTakeInfo): UserTakeInfoModel {
        val managed = UserTakeInfoModel()
        managed.completionTime = takeInfo.completionTime
        managed.id = takeInfo.id
        managed.status = takeInfo.status
        managed.takeTime = takeInfo.takeTime
        return managed
    }

    override fun findUserById(userId: String): User? {
        var foundUser: User? = null
        invoke {
            foundUser = toDataUser(realm.where<UserModel>().equalTo("id", userId).findFirst() ?: return@invoke)
        }
        return foundUser
    }

    private fun toDataUser(realmUser: UserModel): User {
        return realmUser.let { userModel ->
            return@let User(userModel.id,
                userModel.userName,
                userModel.profilePhoto,
                userModel.contactPhone,
                userModel.rating,
                ArrayList(userModel.takenOrders.map { it.let { UserTakeInfo(it.id, it.status, it.takeTime, it.completionTime) } }),
                ArrayList(userModel.takenDeliveries.map { it.let { UserTakeInfo(it.id, it.status, it.takeTime, it.completionTime) } }),
                ArrayList(userModel.takenCares.map { it.let { UserTakeInfo(it.id, it.status, it.takeTime, it.completionTime) } }))
        }
    }

    override fun findUserByUserName(userName: String): User? {
        var foundUser: User? = null
        invoke {
            foundUser = toDataUser(realm.where<UserModel>().equalTo("userName", userName).findFirst() ?: return@invoke)
        }
        return foundUser
    }
}