package ru.innohelpers.innohelp.view_models.profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.innohelpers.innohelp.InnoHelpApplication
import ru.innohelpers.innohelp.component_model.ObservableObject
import ru.innohelpers.innohelp.services.authentication.IUserProvider
import ru.innohelpers.innohelp.view_data.provider.toViewData
import ru.innohelpers.innohelp.view_data.user.UserViewData
import javax.inject.Inject

class ProfileViewModel : ViewModel() {

    init {
        InnoHelpApplication.servicesComponent.inject(this)
    }

    @Inject
    lateinit var userProvider: IUserProvider

    val currentUser = ObservableObject<UserViewData>()
    val isAuthorized = ObservableObject<Boolean>()

    fun loadData(refresh: Boolean = false) {
        GlobalScope.launch {
            if (userProvider.user == null) {
                isAuthorized.value = false
                currentUser.value = null
            } else {
                if (refresh)
                    currentUser.value = userProvider.getUser(userProvider.user!!.id)?.toViewData()
                else
                    currentUser.value = userProvider.user!!.toViewData()
                isAuthorized.value = true
            }
        }
    }

    fun update(picture: String?, phone: String?) {
        if (isAuthorized.value != true) return
        GlobalScope.launch {
            val newData = UserViewData("", picture, phone, 0.0, arrayListOf(), arrayListOf(), arrayListOf())
            userProvider.updateUser(newData)
            loadData(true)
        }
    }
}