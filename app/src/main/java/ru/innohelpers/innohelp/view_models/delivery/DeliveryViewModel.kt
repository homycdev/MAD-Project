package ru.innohelpers.innohelp.view_models.delivery

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.innohelpers.innohelp.InnoHelpApplication
import ru.innohelpers.innohelp.component_model.ObservableObject
import ru.innohelpers.innohelp.component_model.collections.ObservableCollection
import ru.innohelpers.innohelp.data.delivery.Delivery
import ru.innohelpers.innohelp.data.extensions.takeOpen
import ru.innohelpers.innohelp.services.authentication.IUserProvider
import ru.innohelpers.innohelp.services.delivery.IDeliveryProvider
import ru.innohelpers.innohelp.view_data.delivery.DeliveryViewData
import ru.innohelpers.innohelp.view_data.provider.toViewData
import javax.inject.Inject

class DeliveryViewModel : ViewModel() {

    init {
        InnoHelpApplication.servicesComponent.inject(this)
    }

    @Inject
    lateinit var userProvider: IUserProvider

    @Inject
    lateinit var deliveryProvider: IDeliveryProvider

    var currentItem = ObservableObject<DeliveryViewData>()
    var isOwner = ObservableObject<Boolean>()
    var isTaken = ObservableObject<Boolean>()
    var isClosed = ObservableObject<Boolean>()
    var busy = ObservableObject<Boolean>()
    var loadingDeliveries = ObservableObject<Boolean>()
    var allDeliveries: ObservableCollection<DeliveryViewData> = ObservableCollection()

    fun loadAllDeliveries(forceNet: Boolean = false) {
        if (!forceNet && allDeliveries.size != 0) return
        loadingDeliveries.value = true
        GlobalScope.launch {
            val orders = deliveryProvider.getAll(forceNet)
            val sortedDeliveries = orders.takeOpen().sortedByDescending { order -> order.openTime }

            allDeliveries.clear()
            allDeliveries.addAll(sortedDeliveries.toViewData(userProvider))
            loadingDeliveries.value = false
        }
    }

    fun createDelivery(title: String, location: String, items: List<String>, totalCost: Double, benefit: Double) {
        if (userProvider.user == null) return
        busy.value = true
        GlobalScope.launch {
            val delivery = Delivery(
                "", userProvider.user!!.id,
                null, title, location,
                totalCost, benefit, items, null, false
            )
            val id = deliveryProvider.createDelivery(delivery)
            if (id != null) {
                val createdDelivery = deliveryProvider.getDelivery(id, false)!!
                allDeliveries.insert(createdDelivery.toViewData(userProvider), 0)
            }
            busy.value = false
        }
    }

    fun loadDelivery(deliveryId: String, forceNet: Boolean) {
        busy.value = true
        GlobalScope.launch {
            val user = userProvider.user
            val delivery = deliveryProvider.getDelivery(deliveryId, forceNet)!!
            currentItem.value = delivery.toViewData(userProvider)
            if (user == null) {
                isOwner.value = null
                isTaken.value = null
            } else {
                if (delivery.creator == user.id) {
                    isOwner.value = true
                    isTaken.value = null
                } else {
                    isTaken.value = delivery.takenBy != null
                }
            }
            isClosed.value = delivery.closed
            busy.value = false
        }
    }

    fun takeDelivery() {
        val user = userProvider.user
        if (user == null || currentItem.value == null) return
        GlobalScope.launch {
            if (deliveryProvider.takeDelivery(currentItem.value!!.id, user.id)) {
                loadDelivery(currentItem.value!!.id, false)
            }
        }
    }

    fun cancelDelivery() {
        if (userProvider.user == null || currentItem.value == null) return
        GlobalScope.launch {
            if (deliveryProvider.cancelDelivery(currentItem.value!!.id))
                loadDelivery(currentItem.value!!.id, false)
        }
    }

    fun closeDelivery() {
        if (userProvider.user == null || currentItem.value == null) return
        GlobalScope.launch {
            if (deliveryProvider.closeDelivery(currentItem.value!!.id))
                loadDelivery(currentItem.value!!.id, false)
        }
    }

    fun completeDelivery() {
        if (userProvider.user == null || currentItem.value == null) return
        GlobalScope.launch {
            if (deliveryProvider.completeDelivery(currentItem.value!!.id))
                loadDelivery(currentItem.value!!.id, false)
        }
    }

}