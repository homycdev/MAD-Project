package ru.innohelpers.innohelp.view_models.order

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.innohelpers.innohelp.InnoHelpApplication
import ru.innohelpers.innohelp.component_model.ObservableObject
import ru.innohelpers.innohelp.component_model.collections.ObservableCollection
import ru.innohelpers.innohelp.data.extensions.takeOpen
import ru.innohelpers.innohelp.data.order.Order
import ru.innohelpers.innohelp.data.order.OrderItem
import ru.innohelpers.innohelp.services.authentication.IUserProvider
import ru.innohelpers.innohelp.services.orders.IOrdersProvider
import ru.innohelpers.innohelp.view_data.order.OrderViewData
import ru.innohelpers.innohelp.view_data.provider.toViewData
import javax.inject.Inject

class OrdersViewModel : ViewModel() {

    init {
        InnoHelpApplication.servicesComponent.inject(this)
    }

    @Inject
    lateinit var ordersProvider: IOrdersProvider

    @Inject
    lateinit var userProvider: IUserProvider

    var currentItem = ObservableObject<OrderViewData>()
    var isOwner = ObservableObject<Boolean>()
    var isJoined = ObservableObject<Boolean>()
    var isClosed = ObservableObject<Boolean>()
    var busy = ObservableObject<Boolean>()
    var loadingOrders = ObservableObject<Boolean>()
    var allOrders: ObservableCollection<OrderViewData> = ObservableCollection()

    fun loadAllOrders(forceNet: Boolean = false) {
        if (!forceNet && allOrders.size != 0) return
        loadingOrders.value = true
        GlobalScope.launch {
            val orders = ordersProvider.getAllOrders(forceNet)
            val sortedOrders = orders.takeOpen().sortedByDescending { order -> order.openTime }

            allOrders.clear()
            allOrders.addAll(sortedOrders.toViewData(userProvider))
            loadingOrders.value = false
        }
    }

    fun createOrder(title: String, description: String) {
        if (userProvider.user == null) return
        busy.value = true
        GlobalScope.launch {
            val order = Order(
                "",
                null,
                title,
                description,
                userProvider.user!!.id,
                arrayListOf(),
                arrayListOf(),
                0.0
            )
            val id = ordersProvider.createOrder(order)
            if (id != null) {
                val createdOrder = ordersProvider.getOrder(id, false)
                allOrders.insert(createdOrder.toViewData(userProvider), 0)
            }
            busy.value = false
        }
    }

    fun loadOrder(orderId: String, forceNet: Boolean) {
        busy.value = true
        GlobalScope.launch {
            val user = userProvider.user
            val order = ordersProvider.getOrder(orderId, forceNet)
            currentItem.value = order.toViewData(userProvider)
            if (user == null) {
                isOwner.value = null
                isJoined.value = null
            } else {
                if (order.creator == user.id) {
                    isOwner.value = true
                    isJoined.value = null
                } else {
                    isJoined.value = false
                    for (participant in order.participants) {
                        if (participant == user.id) {
                            isJoined.value = true
                            break
                        }
                    }
                }
            }
            isClosed.value = order.closed
            busy.value = false
        }
    }

    fun joinOrder() {
        val user = userProvider.user
        if (user == null || currentItem.value == null) return
        GlobalScope.launch {
            if (ordersProvider.joinOrder(currentItem.value!!.id, user.id)) {
                loadOrder(currentItem.value!!.id, false)
            }
        }
    }

    fun addItem(link: String, description: String, price: Double) {
        if (userProvider.user == null || currentItem.value == null) return
        GlobalScope.launch {
            val orderItem = OrderItem("", userProvider.user!!.id, link, "", description, price)
            ordersProvider.addItem(currentItem.value!!.id, orderItem)
            loadOrder(currentItem.value!!.id, false)
        }
    }

    fun leaveOrder() {
        if (userProvider.user == null || currentItem.value == null) return
        GlobalScope.launch {
            if (ordersProvider.leaveOrder(currentItem.value!!.id, userProvider.user!!.id))
                loadOrder(currentItem.value!!.id, false)
        }
    }

    fun closeOrder() {
        if (userProvider.user == null || currentItem.value == null) return
        GlobalScope.launch {
            if (ordersProvider.closeOrder(currentItem.value!!.id))
                loadOrder(currentItem.value!!.id, false)
        }
    }

}