package ru.innohelpers.innohelp.services.modules

import dagger.Module
import dagger.Provides
import ru.innohelpers.innohelp.services.server.IApiServer
import ru.innohelpers.innohelp.services.server.ApiServer
import javax.inject.Singleton

@Module
class ServerModule {

    @Singleton
    @Provides
    fun provideApiServer(): IApiServer {
        return ApiServer()
    }

}