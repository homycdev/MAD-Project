package ru.innohelpers.innohelp.services

import dagger.Component
import ru.innohelpers.innohelp.services.modules.ProvidersModule
import ru.innohelpers.innohelp.services.modules.ServerModule
import ru.innohelpers.innohelp.services.modules.ViewModelsModule
import javax.inject.Singleton

// @Singleton
// @Component(modules = [ServerModule::class, ProvidersModule::class, ViewModelsModule::class])
// interface ServicesComponent {
//     fun inject(fragment: MainFragmentBase)
//     fun inject(activity: ViewOrderActivity)
//     fun inject(activity: NewOrderActivity)
//     fun inject(activity: MainActivity)
//     fun inject(viewModel: LoginActivityViewModel)
//     fun inject(fragment: AllOrdersFragment)
//     fun inject(viewModel: OrdersViewModel)
// }