package ru.innohelpers.innohelp.services.modules

import dagger.Module
import dagger.Provides
import ru.innohelpers.innohelp.view_models.order.OrdersViewModel
import javax.inject.Singleton


@Module
class ViewModelsModule {

    @Provides
    @Singleton
    fun provideOrdersViewModel(): OrdersViewModel {
        return OrdersViewModel()
    }
}