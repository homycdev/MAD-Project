package ru.innohelpers.innohelp.services.server

import ru.innohelpers.innohelp.data.order.Order
import ru.innohelpers.innohelp.data.order.OrderItem
import ru.innohelpers.innohelp.data.user.User

interface IApiServer {
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