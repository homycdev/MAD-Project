package ru.innohelpers.innohelp.view_data.provider

import ru.innohelpers.innohelp.data.care.Care
import ru.innohelpers.innohelp.data.delivery.Delivery
import ru.innohelpers.innohelp.data.order.Order
import ru.innohelpers.innohelp.data.order.OrderItem
import ru.innohelpers.innohelp.data.user.User
import ru.innohelpers.innohelp.data.user.UserTakeInfo
import ru.innohelpers.innohelp.services.authentication.IUserProvider
import ru.innohelpers.innohelp.view_data.care.CareViewData
import ru.innohelpers.innohelp.view_data.delivery.DeliveryViewData
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

suspend fun Delivery.toViewData(userProvider: IUserProvider): DeliveryViewData {
    val creator = userProvider.getUser(creator)?.toViewData()
    var takenByUser: UserViewData? = null
    if (takenBy != null) takenByUser = userProvider.getUser(takenBy)?.toViewData()

    return DeliveryViewData(
        id,
        openTime!!,
        title,
        location,
        totalCost,
        benefit, creator!!, takenByUser, ArrayList(items),
        closed
    )
}

@JvmName("toViewDataDelivery")
suspend fun Collection<Delivery>.toViewData(userProvider: IUserProvider): Collection<DeliveryViewData> {
    val viewDataCollection = ArrayList<DeliveryViewData>()
    for (delivery in this) viewDataCollection.add(delivery.toViewData(userProvider))
    return viewDataCollection
}

suspend fun Care.toViewData(userProvider: IUserProvider): CareViewData {
    val creator = userProvider.getUser(creator)?.toViewData()
    var takenByUser: UserViewData? = null
    if (takenBy != null) takenByUser = userProvider.getUser(takenBy)?.toViewData()

    return CareViewData(id, openTime, title, description, creator!!, benefit, takenByUser, closed)
}

@JvmName("toViewDataCare")
suspend fun Collection<Care>.toViewData(userProvider: IUserProvider): Collection<CareViewData> {
    val viewDataCollection = ArrayList<CareViewData>()
    for (care in this) viewDataCollection.add(care.toViewData(userProvider))
    return viewDataCollection
}