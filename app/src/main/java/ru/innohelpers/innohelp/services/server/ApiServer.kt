package ru.innohelpers.innohelp.services.server

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.content.*
import io.ktor.http.*
import ru.innohelpers.innohelp.data.order.Order
import ru.innohelpers.innohelp.data.order.OrderItem
import ru.innohelpers.innohelp.data.user.User

class ApiServer : IApiServer {

    private val _serverUrl = "https://inno-helpers.herokuapp.com/api"
    private val _orderApiUrl = "/order/"
    private val _userApiUrl = "/user/"
    private val _createNewOrderMethod = "create"
    private val _getOrderMethod = "get"
    private val _addParticipantToOrderMethod = "addParticipant"
    private val _removeParticipantFromOrderMethod = "removeParticipant"
    private val _addItemToOrderMethod = "addItemToOrder"
    private val _removeItemFromOrderMethod = "removeItemFromOrder"
    private val _closeOrderMethod = "close"
    private val _getAllOrders = "getAll"
    private val _loginUserMethod = "login"
    private val _registerUserMethod = "register"
    private val _updateDataUserMethod = "updateData"
    private val _getUserMethod = "get"
    private val _rateUserMethod = "rateUser"

    override suspend fun createNewOrder(order: Order): String? {
        val requestString = _serverUrl + _orderApiUrl + _createNewOrderMethod
        val jsonString = Gson().toJson(order)

        val client = HttpClient()
        val response = client.post<HttpResponse> {
            url(requestString)
            body = TextContent(jsonString, ContentType.Application.Json)
        }
        if (response.status.value != 200) return null
        return response.readText()
    }

    override suspend fun getOrder(orderId: String): Order {
        val requestString = _serverUrl + _orderApiUrl + _getOrderMethod
        val client = HttpClient()
        val response = client.get<String> {
            url(requestString)
            parameter("orderId", orderId)
        }
        return Gson().fromJson(response, Order::class.java)
    }

    override suspend fun addParticipantToOrder(orderId: String, userId: String): Boolean {
        val requestString = _serverUrl + _orderApiUrl + _addParticipantToOrderMethod
        val client = HttpClient()
        val response = client.get<HttpResponse> {
            url(requestString)
            parameter("orderId", orderId)
            parameter("userId", userId)
        }
        return response.status.value == 200
    }

    override suspend fun removeParticipantToOrder(orderId: String, userId: String): Boolean {
        val requestString = _serverUrl + _orderApiUrl + _removeParticipantFromOrderMethod
        val client = HttpClient()
        val response = client.get<HttpResponse> {
            url(requestString)
            parameter("orderId", orderId)
            parameter("userId", userId)
        }
        return response.status.value == 200
    }

    override suspend fun addItemToOrder(orderId: String, orderItem: OrderItem): String? {
        val requestString = _serverUrl + _orderApiUrl + _addItemToOrderMethod
        val jsonString = Gson().toJson(orderItem)
        val client = HttpClient()
        val response = client.post<HttpResponse> {
            url(requestString)
            parameter("orderId", orderId)
            body = TextContent(jsonString, ContentType.Application.Json)
        }
        if (response.status.value != 200) return null
        return response.readText()
    }

    override suspend fun removeItemFromOrder(orderId: String, itemId: String): Boolean {
        val requestString = _serverUrl + _orderApiUrl + _removeItemFromOrderMethod
        val client = HttpClient()
        val response = client.get<HttpResponse> {
            url(requestString)
            parameter("orderId", orderId)
            parameter("itemId", itemId)
        }
        return response.status.value == 200
    }

    override suspend fun closeOrder(orderId: String): Boolean {
        val requestString = _serverUrl + _orderApiUrl + _closeOrderMethod
        val client = HttpClient()
        val response = client.get<HttpResponse> {
            url(requestString)
            parameter("orderId", orderId)
        }
        return response.status.value == 200
    }

    override suspend fun getAllOrders(): Collection<Order> {
        val requestString = _serverUrl + _orderApiUrl + _getAllOrders
        val client = HttpClient()
        val response = client.get<String> {
            url(requestString)
        }
        val typeToken = object : TypeToken<ArrayList<Order>>() {}.type
        return Gson().fromJson<ArrayList<Order>>(response, typeToken)
    }

    override suspend fun login(userName: String, passwordHash: String): User? {
        val requestString = _serverUrl + _userApiUrl + _loginUserMethod
        val client = HttpClient()
        val response = client.get<HttpResponse> {
            url(requestString)
            parameter("login", userName)
            parameter("passHash", passwordHash)
        }
        if (response.status.value != 200) return null
        return Gson().fromJson(response.readText(), User::class.java)
    }

    override suspend fun registerUser(userName: String, passwordHash: String): Boolean {
        val requestString = _serverUrl + _userApiUrl + _registerUserMethod
        val client = HttpClient()
        val response = client.get<HttpResponse> {
            url(requestString)
            parameter("login", userName)
            parameter("passHash", passwordHash)
        }
        return response.status.value == 200
    }

    override suspend fun editUserData(newUserData: User): Boolean {
        val requestString = _serverUrl + _userApiUrl + _updateDataUserMethod
        val jsonString = Gson().toJson(newUserData)
        val client = HttpClient()
        val response = client.post<HttpResponse> {
            url(requestString)
            body = TextContent(jsonString, ContentType.Application.Json)
        }
        return response.status.value == 200
    }

    override suspend fun getUser(userId: String): User? {
        val requestString = _serverUrl + _userApiUrl + _getUserMethod
        val client = HttpClient()
        val response = client.get<HttpResponse> {
            url(requestString)
            parameter("userId", userId)
        }
        if (response.status.value != 200) return null
        return Gson().fromJson(response.readText(), User::class.java)
    }

    override suspend fun rateUser(userId: String, rate: Int): Boolean {
        val requestString = _serverUrl + _userApiUrl + _rateUserMethod
        val client = HttpClient()
        val response = client.get<HttpResponse> {
            url(requestString)
            parameter("userId", userId)
            parameter("rate", rate)
        }
        return response.status.value == 200
    }
}