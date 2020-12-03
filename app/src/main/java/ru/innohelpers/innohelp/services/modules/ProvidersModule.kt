package ru.innohelpers.innohelp.services.modules

import dagger.Module
import dagger.Provides
import ru.innohelpers.innohelp.services.server.IApiServer
import ru.innohelpers.innohelp.services.orders.IOrdersProvider
import ru.innohelpers.innohelp.services.authentication.IUserProvider
import ru.innohelpers.innohelp.services.authentication.UserProvider
import ru.innohelpers.innohelp.services.orders.OrdersProvider
import javax.inject.Singleton

@Module
class ProvidersModule {

    @Singleton
    @Provides
    fun provideUserProvider(server: IApiServer): IUserProvider {
        return UserProvider(server)
    }

    @Singleton
    @Provides
    fun provideOrderProvider(server: IApiServer): IOrdersProvider {
        return OrdersProvider(server)
    }

}