package ru.innohelpers.innohelp.view_models

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.innohelpers.innohelp.InnoHelpApplication
import ru.innohelpers.innohelp.component_model.ObservableObject
import ru.innohelpers.innohelp.data.user.User
import ru.innohelpers.innohelp.services.authentication.IUserProvider
import javax.inject.Inject

class LoginActivityViewModel : ViewModel() {

    init {
        InnoHelpApplication.servicesComponent.inject(this)
    }

    var user = ObservableObject<User>()
    var busy = ObservableObject<Boolean>()

    @Inject
    lateinit var userProvider: IUserProvider

    fun login(userName: String, password: String) {
        busy.value = true
        GlobalScope.launch {
            if (userProvider.user != null && userProvider.user!!.userName == userName) {
                user.value = userProvider.user
                return@launch
            }
            userProvider.login(userName, password)
            user.value = userProvider.user
            busy.value = false
        }
    }

    fun register(userName: String, password: String) {
        busy.value = true
        GlobalScope.launch {
            userProvider.register(userName, password)
            user.value = userProvider.user
            busy.value = false
        }
    }

}