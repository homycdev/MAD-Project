package ru.innohelpers.innohelp.services.modules

import dagger.Module
import dagger.Provides
import ru.innohelpers.innohelp.view_data.delivery.DeliveryViewData
import ru.innohelpers.innohelp.view_models.care.CareViewModel
import ru.innohelpers.innohelp.view_models.delivery.DeliveryViewModel
import ru.innohelpers.innohelp.view_models.order.OrdersViewModel
import ru.innohelpers.innohelp.view_models.profile.ProfileViewModel
import javax.inject.Singleton


@Module
class ViewModelsModule {

    @Provides
    @Singleton
    fun provideOrdersViewModel(): OrdersViewModel {
        return OrdersViewModel()
    }

    @Provides
    @Singleton
    fun provideDeliveryViewModel(): DeliveryViewModel {
        return DeliveryViewModel()
    }

    @Provides
    @Singleton
    fun provideCareViewModel(): CareViewModel {
        return CareViewModel()
    }

    @Provides
    @Singleton
    fun provideProfileViewModel(): ProfileViewModel {
        return ProfileViewModel()
    }
}