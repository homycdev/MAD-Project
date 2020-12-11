package ru.innohelpers.innohelp.view_data.provider

import ru.innohelpers.innohelp.data.order.Order
import ru.innohelpers.innohelp.data.order.OrderItem
import ru.innohelpers.innohelp.data.user.User
import ru.innohelpers.innohelp.data.user.UserTakeInfo
import ru.innohelpers.innohelp.services.authentication.IUserProvider
import ru.innohelpers.innohelp.view_data.order.OrderItemViewData
import ru.innohelpers.innohelp.view_data.order.OrderViewData
import ru.innohelpers.innohelp.view_data.user.UserTakeViewData
import ru.innohelpers.innohelp.view_data.user.UserViewData

fun UserTakeInfo.toViewData(): UserTakeViewData {
    return UserTakeViewData(status, takeTime, completionTime)
}

fun User.toViewData(): UserViewData {
    val viewData = UserViewData(userName, profilePhoto, contactPhone, rating, ArrayList(), ArrayList(), ArrayList())
    for (userTake in takenOrders) viewData.takenOrders.add(userTake.toViewData())
    return viewData
}

suspend fun OrderItem.toViewData(userProvider: IUserProvider): OrderItemViewData {
    val ownerUser = userProvider.getUser(addedBy)!!
    return OrderItemViewData(id, title, link, description, ownerUser.toViewData(), price)
}

suspend fun Collection<Order>.toViewData(userProvider: IUserProvider): Collection<OrderViewData> {
    val result = ArrayList<OrderViewData>()
    for (order in this) result.add(order.toViewData(userProvider))
    return result
}

suspend fun Order.toViewData(userProvider: IUserProvider): OrderViewData {
    val ownerUser = userProvider.getUser(creator)!!
    val viewData = OrderViewData(id, openTime!!,
        if (title == null) "" else title!!,
        if (description == null) "" else description!!,
        ownerUser.toViewData(), ArrayList(), ArrayList(), totalPrice, closed)

    for (participant in participants) viewData.participants.add(userProvider.getUser(participant)!!.toViewData())
    for (orderItem in items) viewData.items.add(orderItem.toViewData(userProvider))

    return viewData
}