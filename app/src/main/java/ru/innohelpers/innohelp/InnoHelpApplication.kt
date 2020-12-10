package ru.innohelpers.innohelp

import android.app.Application
import ru.innohelpers.innohelp.services.DaggerServicesComponent
import ru.innohelpers.innohelp.services.ServicesComponent
import ru.innohelpers.innohelp.services.modules.ProvidersModule
import ru.innohelpers.innohelp.services.modules.ServerModule

class InnoHelpApplication : Application() {


    companion object {
        private var component: ServicesComponent? = null

        val servicesComponent: ServicesComponent
            get() {
                if (component == null)
                    component = DaggerServicesComponent.builder()
                        .providersModule(ProvidersModule())
                        .serverModule(ServerModule())
                        .build()
                return component!!
            }
    }
}