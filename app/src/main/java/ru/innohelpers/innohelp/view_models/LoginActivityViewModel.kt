package ru.innohelpers.innohelp.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.apache.commons.codec.digest.DigestUtils
import ru.innohelpers.innohelp.InnoHelpApplication
import ru.innohelpers.innohelp.data.user.User
import ru.innohelpers.innohelp.services.authentication.IUserProvider
import javax.inject.Inject

class LoginActivityViewModel : ViewModel() {

    init {
        InnoHelpApplication.servicesComponent.inject(this)
    }

    var user: MutableLiveData<User?> = MutableLiveData()
    var busy: MutableLiveData<Boolean> = MutableLiveData(false)

    @Inject
    lateinit var userProvider: IUserProvider

    fun login(userName: String, password: String) {
        busy.value = true
        GlobalScope.launch {
            if (userProvider.user != null && userProvider.user!!.userName == userName) {
                user.postValue(userProvider.user)
                return@launch
            }
            val passHash = calculateHash(password)
            userProvider.login(userName, passHash)
            user.postValue(userProvider.user)
            busy.postValue(false)
        }
    }

    fun register(userName: String, password: String) {
        busy.value = true
        GlobalScope.launch {
            val passHash = calculateHash(password)
            userProvider.register(userName, passHash)
            user.postValue(userProvider.user)
            busy.postValue(false)
        }
    }

    private fun calculateHash(password: String): String {
        return DigestUtils.md5Hex(password)
    }
}