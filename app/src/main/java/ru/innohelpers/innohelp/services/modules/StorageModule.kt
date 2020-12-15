package ru.innohelpers.innohelp.services.modules

import android.os.Handler
import dagger.Module
import dagger.Provides
import io.realm.Realm
import ru.innohelpers.innohelp.realm.storage.*
import javax.inject.Singleton

@Module
class StorageModule(private val handler: Handler, private val realm: Realm) {

    @Singleton
    @Provides
    fun provideUserStorage(): IUserStorage {
        return UserRealmStorage(handler, realm)
    }

    @Singleton
    @Provides
    fun provideOrderStorage(): IOrderStorage {
        return OrderRealmStorage(handler, realm)
    }

    @Singleton
    @Provides
    fun provideDeliveryStorage(): IDeliveryStorage {
        return DeliveryRealmStorage(handler, realm)
    }

    @Singleton
    @Provides
    fun provideCareStorage(): ICareStorage {
        return CareRealmStorage(handler, realm)
    }

}