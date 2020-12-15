package ru.innohelpers.innohelp.services.modules

import dagger.Module
import dagger.Provides
import ru.innohelpers.innohelp.realm.storage.ICareStorage
import ru.innohelpers.innohelp.realm.storage.IDeliveryStorage
import ru.innohelpers.innohelp.realm.storage.IOrderStorage
import ru.innohelpers.innohelp.realm.storage.IUserStorage
import ru.innohelpers.innohelp.services.server.IApiServer
import ru.innohelpers.innohelp.services.orders.IOrdersProvider
import ru.innohelpers.innohelp.services.authentication.IUserProvider
import ru.innohelpers.innohelp.services.authentication.UserProvider
import ru.innohelpers.innohelp.services.care.CareProvider
import ru.innohelpers.innohelp.services.care.ICareProvider
import ru.innohelpers.innohelp.services.delivery.DeliveryProvider
import ru.innohelpers.innohelp.services.delivery.IDeliveryProvider
import ru.innohelpers.innohelp.services.orders.OrdersProvider
import javax.inject.Singleton

@Module
class ProvidersModule {

    @Singleton
    @Provides
    fun provideUserProvider(server: IApiServer, storage: IUserStorage): IUserProvider {
        return UserProvider(server, storage)
    }

    @Singleton
    @Provides
    fun provideOrderProvider(server: IApiServer, storage: IOrderStorage): IOrdersProvider {
        return OrdersProvider(server, storage)
    }

    @Singleton
    @Provides
    fun provideDeliveryProvider(server: IApiServer, storage: IDeliveryStorage): IDeliveryProvider {
        return DeliveryProvider(server, storage)
    }

    @Singleton
    @Provides
    fun provideCareProvider(server: IApiServer, storage: ICareStorage): ICareProvider {
        return CareProvider(server, storage)
    }

}