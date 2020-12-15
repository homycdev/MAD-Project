package ru.innohelpers.innohelp.services

import dagger.Component
import ru.innohelpers.innohelp.activity.*
import ru.innohelpers.innohelp.fragments.AllOrdersFragment
import ru.innohelpers.innohelp.fragments.MainFragmentBase
import ru.innohelpers.innohelp.services.modules.ProvidersModule
import ru.innohelpers.innohelp.services.modules.ServerModule
import ru.innohelpers.innohelp.services.modules.StorageModule
import ru.innohelpers.innohelp.services.modules.ViewModelsModule
import ru.innohelpers.innohelp.view_models.LoginActivityViewModel
import ru.innohelpers.innohelp.view_models.care.CareViewModel
import ru.innohelpers.innohelp.view_models.delivery.DeliveryViewModel
import ru.innohelpers.innohelp.view_models.order.OrdersViewModel
import ru.innohelpers.innohelp.view_models.profile.ProfileViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [ServerModule::class, ProvidersModule::class, ViewModelsModule::class, StorageModule::class])
interface ServicesComponent {
    fun inject(viewModel: ProfileViewModel)
    fun inject(activity: ViewCareActivity)
    fun inject(activity: NewCareActivity)
    fun inject(viewModel: CareViewModel)
    fun inject(activity: ViewDeliveryActivity)
    fun inject(viewModel: DeliveryViewModel)
    fun inject(fragment: MainFragmentBase)
    fun inject(activity: ViewOrderActivity)
    fun inject(activity: NewOrderActivity)
    fun inject(activity: NewDeliveryActivity)
    fun inject(activity: MainActivity)
    fun inject(viewModel: LoginActivityViewModel)
    fun inject(fragment: AllOrdersFragment)
    fun inject(viewModel: OrdersViewModel)
}