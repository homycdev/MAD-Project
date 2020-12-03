package ru.innohelpers.innohelp.services.delivery

import ru.innohelpers.innohelp.data.delivery.Delivery

interface IDeliveryProvider {
    suspend fun getAll(): Collection<Delivery>
}