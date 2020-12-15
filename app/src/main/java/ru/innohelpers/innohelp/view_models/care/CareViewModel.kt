package ru.innohelpers.innohelp.view_models.care

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.innohelpers.innohelp.InnoHelpApplication
import ru.innohelpers.innohelp.component_model.ObservableObject
import ru.innohelpers.innohelp.component_model.collections.ObservableCollection
import ru.innohelpers.innohelp.data.care.Care
import ru.innohelpers.innohelp.data.extensions.takeOpen
import ru.innohelpers.innohelp.services.authentication.IUserProvider
import ru.innohelpers.innohelp.services.care.ICareProvider
import ru.innohelpers.innohelp.view_data.care.CareViewData
import ru.innohelpers.innohelp.view_data.provider.toViewData
import javax.inject.Inject

class CareViewModel : ViewModel() {

    init {
        InnoHelpApplication.servicesComponent.inject(this)
    }

    @Inject
    lateinit var userProvider: IUserProvider

    @Inject
    lateinit var careProvider: ICareProvider

    var currentItem = ObservableObject<CareViewData>()
    var isOwner = ObservableObject<Boolean>()
    var isTaken = ObservableObject<Boolean>()
    var isClosed = ObservableObject<Boolean>()
    var busy = ObservableObject<Boolean>()
    var loadingCares = ObservableObject<Boolean>()
    var allCares: ObservableCollection<CareViewData> = ObservableCollection()

    fun loadAllCares(forceNet: Boolean = false) {
        if (!forceNet && allCares.size != 0) return
        loadingCares.value = true
        GlobalScope.launch {
            val cares = careProvider.getAll(forceNet)
            val sortedCares = cares.takeOpen().sortedByDescending { order -> order.openTime }

            allCares.clear()
            allCares.addAll(sortedCares.toViewData(userProvider))
            loadingCares.value = false
        }
    }

    fun createCare(title: String, description: String, benefit: Double) {
        if (userProvider.user == null) return
        busy.value = true
        GlobalScope.launch {
            val care = Care("", null, title, description, userProvider.user!!.id, benefit, null, false)
            val id = careProvider.createCare(care)
            if (id != null) {
                val createdCare = careProvider.getCare(id, false)!!
                allCares.insert(createdCare.toViewData(userProvider), 0)
            }
            busy.value = false
        }
    }

    fun loadCare(careId: String, forceNet: Boolean) {
        busy.value = true
        GlobalScope.launch {
            val user = userProvider.user
            val care = careProvider.getCare(careId, forceNet)!!
            currentItem.value = care.toViewData(userProvider)
            if (user == null) {
                isOwner.value = null
                isTaken.value = null
            } else {
                if (care.creator == user.id) {
                    isOwner.value = true
                    isTaken.value = null
                } else {
                    isTaken.value = care.takenBy != null
                }
            }
            isClosed.value = care.closed
            busy.value = false
        }
    }

    fun takeDelivery() {
        val user = userProvider.user
        if (user == null || currentItem.value == null) return
        GlobalScope.launch {
            if (careProvider.takeCare(currentItem.value!!.id, user.id)) {
                loadCare(currentItem.value!!.id, false)
            }
        }
    }

    fun cancelDelivery() {
        if (userProvider.user == null || currentItem.value == null) return
        GlobalScope.launch {
            if (careProvider.cancelCare(currentItem.value!!.id))
                loadCare(currentItem.value!!.id, false)
        }
    }

    fun closeDelivery() {
        if (userProvider.user == null || currentItem.value == null) return
        GlobalScope.launch {
            if (careProvider.closeCare(currentItem.value!!.id))
                loadCare(currentItem.value!!.id, false)
        }
    }

    fun completeDelivery() {
        if (userProvider.user == null || currentItem.value == null) return
        GlobalScope.launch {
            if (careProvider.completeCare(currentItem.value!!.id))
                loadCare(currentItem.value!!.id, false)
        }
    }

}