package ru.innohelpers.innohelp.services.server

import ru.innohelpers.innohelp.data.care.Care
import ru.innohelpers.innohelp.data.delivery.Delivery
import ru.innohelpers.innohelp.data.order.Order
import ru.innohelpers.innohelp.data.order.OrderItem
import ru.innohelpers.innohelp.data.user.User

interface IApiServer {
    suspend fun createCare(care: Care): String?
    suspend fun completeCare(careId: String): Boolean
    suspend fun cancelCare(careId: String): Boolean
    suspend fun closeCare(careId: String): Boolean
    suspend fun takeCare(careId: String, userId: String): Boolean
    suspend fun getCare(careId: String): Care?
    suspend fun getAllCares(): Collection<Care>

    suspend fun createDelivery(delivery: Delivery): String?
    suspend fun completeDelivery(deliveryId: String): Boolean
    suspend fun cancelDelivery(deliveryId: String): Boolean
    suspend fun closeDelivery(deliveryId: String): Boolean
    suspend fun takeDelivery(deliveryId: String, userId: String): Boolean
    suspend fun getDelivery(deliveryId: String): Delivery?
    suspend fun getAllDeliveries(): Collection<Delivery>

    suspend fun createNewOrder(order: Order): String?
    suspend fun getOrder(orderId: String): Order
    suspend fun addParticipantToOrder(orderId: String, userId: String): Boolean
    suspend fun removeParticipantToOrder(orderId: String, userId: String): Boolean
    suspend fun addItemToOrder(orderId: String, orderItem: OrderItem): String?
    suspend fun removeItemFromOrder(orderId: String, itemId: String): Boolean
    suspend fun closeOrder(orderId: String): Boolean
    suspend fun getAllOrders(): Collection<Order>

    suspend fun login(userName: String, passwordHash: String): User?
    suspend fun registerUser(userName: String, passwordHash: String): Boolean
    suspend fun editUserData(newUserData: User): Boolean
    suspend fun getUser(userId: String): User?
    suspend fun rateUser(userId: String, rate: Int): Boolean
}